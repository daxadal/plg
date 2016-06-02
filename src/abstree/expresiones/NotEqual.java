package src.abstree.expresiones;

import src.abstree.tipos.Bool;
import src.abstree.tipos.Tipo;
import src.errors.UnsuportedOperation;

public class NotEqual extends ExpresionBinaria{

	public NotEqual(Expresion op1, Expresion op2) {
		super(op1, op2);
	}

	@Override
	public TipoE tipo() {
		return TipoE.NEQ;
	}
	
	public Tipo getTipo() throws UnsuportedOperation {
		if(op1.getTipo()==new Bool())
			return new Bool();
		else throw new UnsuportedOperation("Negación sin Booleanos.");
	}

}
