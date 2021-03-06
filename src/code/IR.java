package code;

import java.util.LinkedList;

import errors.CompilingException;
import abstree.expresiones.TipoE;

/**
 * Clase InstructionRepertory que reune todas las instrucciones m�quina y 
 * las centraliza para que su modificaci�n sea m�s sencilla. Actualmente, 
 * los saltos que se usan en las sentencias son saltos relativos, que se transforman
 *  en absolutos con la funcion {@link IR#relToAbsJumps(LinkedList)}
 */
public class IR {
	public static String add(){return "add;";}
	public static String sub(){return "sub;";}
	public static String mul(){return "mul;";}
	public static String div(){return "div;";}
	public static String neg(){return "neg;";}
	
	public static String and(){return "and;";}
	public static String or(){return "or;";}
	public static String not(){return "not;";}

	public static String eq(){return "equ;";}
	public static String ge(){return "geq;";}
	public static String le(){return "leq;";}
	public static String lt(){return "les;";}
	public static String gt(){return "grt;";}
	public static String neq(){return "neq;";}
	
	public static String ldcAddr(int c){return "lda 0 "+c+";";}
	public static String ldcInt(int c){return "ldc "+c+";";}
	public static String ldcTrue(){return "ldc true;";}
	public static String ldcFalse(){return "ldc false;";}
	public static String ind(){return "ind;";}
	public static String sto(){return "sto;";}
	
	public static String load0(){return "ldo 0;";}
	public static String store0(){return "sro 0;";}
	public static String load1(){return "ldo 1;";}
	public static String store1(){return "sro 1;";}
	
	public static String dup(){return "dpl;";}
	
	/**
	 * Instruccion de salto a una funci�n
	 * @param lparam longitud de la zona de parametros de la funcion llamada.
	 * @param dir direccion de comienzo del c�digo de la funcion llamada.
	 * @return
	 */
	public static String callj(int lparam, int dir){return "cup "+lparam+" "+dir+";";}
	public static String returnj(){return "retp;";}
	public static String uncondj(int dir){return "ujp "+dir+";";}
	public static String condj(int dir){return "fjp "+dir+";";}
	public static String casej(int dir){return "ixj "+dir+";";}

	
	public static String startfun(int lvar){return "ssp "+lvar+";";}
	public static String startcall(){return "mst 0;";}
	public static String movs(int size){return "movs "+size+";";}
	public static String access(int tam){return "ixa "+tam+";";}
	public static String stop(){return "stp;";}
	
	public static String inc(int n){return "inc "+n+";";}
	public static String dec(int n){return "dec "+n+";";}
	
	/**
	 * Obtiene la instruccion correspondiente a una expresion binaria
	 * @param t Tipo de la expresi�n
	 * @return Instrucci�n m�quina correspondiente
	 * @throws CompilingException si el tipo no se corresponde con el de
	 * una expresion binaria admitida
	 */
	public static String binary(TipoE t) throws CompilingException{
		String ins;
		switch (t){

		case AND: ins = IR.and();
			break;
		case DIV: ins = IR.div();
			break;
		case EQ: ins = IR.eq();
			break;
		case GE: ins = IR.ge();
			break;
		case GT: ins = IR.gt();
			break;
		case LE: ins = IR.le();
			break;
		case LT: ins = IR.lt();
			break;
		case MULT: ins = IR.mul();
			break;
		case NEQ: ins = IR.neq();
			break;
		case OR: ins = IR.or();
			break;
		case RESTA: ins = IR.sub();
			break;
		case SUMA: ins = IR.add();
			break;
		default: throw new CompilingException("Instruccion binaria no reconocida");
		
		}
		return ins;
	}
	
	/**
	 * Transforma todos los saltos de este bloque de relativos a absolutos,
	 * tomando como instrucci�n cero la primera de este bloque.
	 * @param code Bloque de codigo original
	 * @return Bloque de c�digo transformado
	 */
	public static LinkedList<String> relToAbsJumps(LinkedList<String> code){
		LinkedList<String> newcode= new LinkedList<String>();
		int i=0;
		String s;
		int dir;
		for (String instr: code){
			if (instr.length()>4) {
				s = (String) instr.subSequence(0, 4);
				if (s.equalsIgnoreCase("ujp ") 
						|| s.equalsIgnoreCase("fjp ")
						|| s.equalsIgnoreCase("ixj ")
					)
				{
					dir = Integer.parseInt(instr.substring(4, instr.length()-1));
					dir = dir + i;
					instr = s+dir+";";
				}
			}
			newcode.add(instr);
			i++;
		}
		return newcode;
	}
	
	/**
	 * Dado que el main en el codigo maquina va antes que las funciones,
	 * a�ade el tama�o del main a los saltos a funciones.
	 * Las 5 primeras instrucciones (comunes a todos los programas)
	 * no se modifican
	 * @param code Bloque de codigo original
	 * @param tam tama�o del main, a sumar a los saltos a funciones
	 * @return Bloque de codigo transformado
	 */
	public static LinkedList<String> adjustFuncionJumps(LinkedList<String> code, int tam){
		LinkedList<String> newcode= new LinkedList<String>();
		newcode.add(code.removeFirst());
		newcode.add(code.removeFirst());
		newcode.add(code.removeFirst());
		newcode.add(code.removeFirst());
		newcode.add(code.removeFirst());
		String s;
		String[] param;
		int dir, lparam;
		for (String instr: code){
			if (instr.length()>4) {
				s = (String) instr.subSequence(0, 4);
				if ( s.equalsIgnoreCase("cup ") ){
					//XXX TEST System.out.print(instr);
					param = instr.substring(0, instr.length()-1).split(" ");
					//XXX TEST for(String item: param) System.out.print("["+item+"]");
					lparam = Integer.parseInt(param[1]);
					dir = Integer.parseInt(param[2]);
					dir = dir + tam;
					instr = s+lparam+' '+dir+";";
					//XXX TEST System.out.println(" cambiado a "+instr);
				}
			}
			newcode.add(instr);
		}
		return newcode;
	}
	
	/**
	 * Guarda un array en memoria. deben estar los datos de 0 (fondo)
	 * a tam-1 (cima) y encima la direccion.
	 * @param tam tama�o del array
	 * @return
	 */
	public static LinkedList<String> multipleStore(int tam){
		LinkedList<String> code = new LinkedList<String>();
		
		code.add(IR.inc(tam-1)); //Aumentamos la direccion hasta la ultima posicion del array
		for(int i=tam-1;i>=1;i--){
			code.add(IR.store0());
			code.add(IR.store1());
			code.add(IR.load0());
			code.add(IR.load1());
			code.add(IR.sto()); //Tras ordenar direccion y valor, guardamos
			code.add(IR.load0());
			code.add(IR.dec(1));//Decrementamos para que apunte a la posicion anterior
		}
		code.add(IR.store0());
		code.add(IR.store1());
		code.add(IR.load0());
		code.add(IR.load1());
		code.add(IR.sto()); //Tras ordenar direccion y valor, guardamos
		
		return code;
	}
}
