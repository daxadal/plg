package src.abstree.expresiones;

import src.abstree.tipos.ArrayOf;
import src.abstree.tipos.Bool;
import src.abstree.tipos.Int;
import src.abstree.tipos.Tipo;
import src.resolid.Visitante;
import src.errors.UnsuportedOperation;

public class AllTo extends Expresion{

	
	public AllTo(Expresion op1, int num) {
		this.op1 = op1;
		this.num = num;
		this.fila = op1.getFila();
	}
	
	@Override
	public int num() throws UnsuportedOperation {
		return num;
	}
	

	@Override
	public Expresion op1() throws UnsuportedOperation {
		return op1;
	}
	
	@Override
	public TipoE tipo() {
		return TipoE.ALLTO;
	}
	
	public Tipo getTipo() throws UnsuportedOperation {
		int valor = op1.getTipo().valorT();
		int fila = op1.getFila();
		
		if(valor%2==0){
			Int test = new Int();
			if(valor==test.valorT())
				return new ArrayOf(num,new Int(),fila);
			else return findDim(valor,num,new Int(),fila);			
		} else {
			Bool testB = new Bool();
			if (valor==testB.valorT())
				return new ArrayOf(num,new Bool(),fila);
			else return findDim(valor,num,new Bool(),fila);
		}
	}
	
	private Tipo findDim(int valor, int num, Tipo tipo, int fila) {
		if(valor>=4)
			return new ArrayOf(num,findDim(valor-2,num,tipo,fila),fila);
		else return new ArrayOf(num,tipo,fila);
	}

	@Override
	public void accept(Visitante v) {
		v.previsit(this);
		v.visit(num);
		op1.accept(v);
		v.postvisit(this);
	}

	private Expresion op1;
	private int num;

}
