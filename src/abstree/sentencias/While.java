package abstree.sentencias;

import errors.GestionErroresTiny;
import errors.UnsuportedOperation;
import abstree.Programa;
import abstree.expresiones.Expresion;
import abstree.tipos.Bool;
import resolid.Visitante;

public class While extends Sentencia {

	/**
	 * Crea sentencias <code>while-do-done</code>
	 * @param cond condicion del while
	 * @param code cuerpo del while
	 */
	public While(Expresion cond, Programa code){
		this.cond = cond;
		this.code = code;
	}
	@Override
	public TipoS tipo() {
		return TipoS.WHILE;
	}
	@Override
	public Expresion exp() throws UnsuportedOperation {
		return cond;
	}
	@Override
	public Programa codeAt(int i) throws UnsuportedOperation {
		if (i==0) return code;
		else throw new UnsuportedOperation("code "+i);
	}
	
	@Override
	public void accept(Visitante v) {
		v.previsit(this);
		cond.accept(v);
		code.accept(v);
		v.postvisit(this);
	}


	public boolean checkTipo() {
		boolean ret = true;
		try {
			if (cond.getTipo().valorT()!=(new Bool()).valorT()){
				ret = false;
				GestionErroresTiny.errorTipos(cond.getFila(),"If de condicion no booleana.");
			}
		} catch (UnsuportedOperation e) {
			ret = false;
			GestionErroresTiny.errorTipos(cond.getFila(), e.getLocalizedMessage());
		}
		if (!code.checkTipo())
			ret = false;

		return ret;
	}
	
	private Expresion cond;
	private Programa code;

}
