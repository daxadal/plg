package abstree.expresiones;

public class LowerThan extends ExpresionBinaria {

	public LowerThan(Expresion op1, Expresion op2) {
		super(op1, op2);
	}

	@Override
	public TipoE tipo() {
		return TipoE.LT;
	}

	public Tipo getTipo() throws UnsuportedOperation {
		if(op1.getTipo()==new Int() && op2.getTipo()==new Int())
			return new Bool();
		else throw new UnsuportedOperation("Igualdad con no enteros(<).");
	}
}
