package src.abstree.expresiones;

import java.util.LinkedList;

import src.abstree.tipos.ArrayOf;
import src.abstree.tipos.Tipo;
import src.resolid.Visitante;
import src.errors.UnsuportedOperation;

public class ArrayWithKeys extends Expresion {

	public ArrayWithKeys(LinkedList<Expresion> array){
		this.array = array;
	}
	
	public ArrayWithKeys(LinkedList<Expresion> array, int fila){
		this.array = array;
		this.fila = fila;
	}
	
	@Override
	public TipoE tipo() {
		return TipoE.ARRAY;
	}
	
	public Expresion elemAt(int i) throws UnsuportedOperation {
		try {
			return array.get(i);
		}catch(IndexOutOfBoundsException e){
			throw new UnsuportedOperation("elemAt" +i);
		}
	}
	public int num() throws UnsuportedOperation {
		return array.size();
	}
	
	private LinkedList<Expresion> array;
	


	@Override
	public void accept(Visitante v) {
		v.previsit(this);
		for(Expresion e:array)
			e.accept(v);
		v.postvisit(this);
	}

	public Tipo getTipo() throws UnsuportedOperation {
		Tipo test = array.get(0).getTipo();
		for(int i=1;i<array.size();i++)
			if(test.valorT()!=array.get(i).getTipo().valorT())
				throw new UnsuportedOperation("Constructor de array inconsistente.");
			
		return new ArrayOf(array.size(),test,fila);
	}

}
