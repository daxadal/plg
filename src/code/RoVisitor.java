package code;

import java.util.Hashtable;
import java.util.Stack;

import resolid.VisitorHelper;
import abstree.Declaracion;
import abstree.Funcion;
import abstree.Programa;
import abstree.expresiones.Identificador;
import errors.GestionErroresTiny;
import errors.UnsuportedOperation;

/**

 * Recorre el �rbol, asignando direcciones de memoria a las variables
 * del programa. La organizaci�n de la memoria es la siguiente:
 * <ul>
 * <li> Las direcciones son relativas al marco de pila
 * <li> Las direcciones de 0 a 4 est�n reservadas
 * <li> Se asignan las  direcciones a las variables de entrada a 
 * partir de la 5, teniendo en cuenta los tama�os.
 * <li> A continuacion se asignan las variables de salida. Se deja una
 * palabra libre para la direccion de la variable de salida del procedimiento
 * llamante ( ro(declSalida)-1 ).
 * <li> Por �ltimo, se asignan las variables del programa de los distintos bloques,
 * donde dos bloques hermanos comparten memoria.
 * </ul>
 */
public class RoVisitor extends VisitorHelper{

	private Hashtable<Declaracion,Integer> ro;
	private Hashtable<Funcion,Integer> lparam;
	private Hashtable<Funcion,Integer> lvar;
	private int nextdir;
	private int maxdir;
	private Stack<Integer> dirstack;
	
	public String toString(){
		return "RO: " + ro.toString() + '\n'
				+ "lparam: " + lparam.toString() + '\n'
				+ "lvar: " + lvar.toString() + '\n';
	}
	
	public RoVisitor(){
		this.ro = new Hashtable<Declaracion,Integer>();
		this.lparam = new Hashtable<Funcion,Integer>();
		this.lvar = new Hashtable<Funcion,Integer>();
		this.nextdir = 5;
		this.maxdir = 4;
		this.dirstack = new Stack<Integer>();
	}
	
	/**
	 * Devuelve la direcci�n de memoria asociada a la declaracion
	 * de este identificador
	 * @param id Identificador de la variable
	 * @return Direccion de memoria
	 */
	public int ro(Identificador id){
		int ret = -666;
		try {
			ret = ro.get(id.ref());
		} catch (UnsuportedOperation e) {
			e.printStackTrace();
		}
		if (ret<=0)
			GestionErroresTiny.errorRO(id.getFila(), "Variable " + id + "no encontrada");
		return ret;
	}
	
	/**
	 * Devuelve la direcci�n de memoria asociada a esta declaracion
	 * @param id Declaracion de la variable
	 * @return Direccion de memoria
	 */
	public int ro(Declaracion id){
		Integer ret = ro.get(id);
		if (ret == null) {
			GestionErroresTiny.errorRO(id.getFila(), "Variable " + id + "no encontrada");
			return -666;
		}
		else
			return ret;
	}
	
	/**
	 * Devuelve la longitud de la zona de par�metros de la funcion
	 * @param f Declaracion de la funcion
	 * @return Tama�o de los par�metros en memoria
	 */
	public int lparam(Funcion f){
		Integer ret = lparam.get(f);
		if (ret == null) {
			GestionErroresTiny.errorRO(f.getFila(), "Funcion " + f + "no encontrada");
			return -333;
		}
		else
			return ret;
	}
	
	/**
	 * Devuelve la longitud de la zona de organizacion,
	 * par�metros y variables (en todos los bloques) de la funcion
	 * @param f Declaracion de la funcion
	 * @return Tama�o de los par�metros en memoria
	 */
	public int lvar(Funcion f){
		Integer ret = lvar.get(f);
		if (ret == null) {
			GestionErroresTiny.errorRO(f.getFila(), "Funcion " + f + "no encontrada");
			return -777;
		}
		else
			return ret;
	}
	
	/**
	 * Devuelve la longitud de la zona de organizacion,
	 *  y variables (en todos los bloques) del Main
	 * @return Tama�o de las variables en memoria
	 */
	public int lvarMain(){
		return this.maxdir+1;
	}

	@Override
	public void previsit(Declaracion node) {
		this.ro.put(node, this.nextdir);
		this.nextdir = this.nextdir + node.getTipo().tam();
		if(nextdir-1 > maxdir)
			maxdir = nextdir-1;
	}

	@Override
	public boolean previsit(Funcion node) {
		this.nextdir = 5;
		this.maxdir = 4;
		
		for (Declaracion d: node.getEntrada()){
			this.ro.put(d, this.nextdir);
			this.nextdir = this.nextdir + d.getTipo().tam();
		}
		for (Declaracion d: node.getSalida()){
			this.ro.put(d, this.nextdir+1);
			this.nextdir = this.nextdir + d.getTipo().tam()+1;
		}
		this.lparam.put(node, this.nextdir-5);
		this.maxdir = this.nextdir-1;
		
		node.getPrograma().accept(this);
		
		this.lvar.put(node, this.maxdir+1);
		this.nextdir = 5;
		this.maxdir = 4;
		return false;
	}

	@Override
	public void previsit(Programa node) {
		this.dirstack.push(this.nextdir);
	}

	@Override
	public void postvisit(Programa node) {
		this.nextdir = this.dirstack.pop();
	}
}
