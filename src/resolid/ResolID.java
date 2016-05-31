package resolid;

import errors.IdentifyingIdException;
import errors.UnsuportedOperation;
import abstree.Codigo;
import abstree.Declaracion;
import abstree.Funcion;
import abstree.Programa;
import abstree.expresiones.AllTo;
import abstree.expresiones.ArrayWithKeys;
import abstree.expresiones.ExpresionBinaria;
import abstree.expresiones.ExpresionUnaria;
import abstree.expresiones.False;
import abstree.expresiones.Identificador;
import abstree.expresiones.Number;
import abstree.expresiones.True;
import abstree.sentencias.Asignacion;
import abstree.sentencias.Call;
import abstree.sentencias.Choose;
import abstree.sentencias.IfThenElse;
import abstree.sentencias.While;
import abstree.tipos.ArrayOf;
import abstree.tipos.Bool;
import abstree.tipos.Int;

public class ResolID implements Visitante {

	private TablaDeSimbolos tabla;
	
	public ResolID() {
		this.tabla = new TablaDeSimbolos();
	}
	
	@Override
	public void previsit(Declaracion node) {
		try {
			tabla.insertaIdV(node.getId(), node);
		} catch (IdentifyingIdException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void previsit(Funcion node) {
		try {
			tabla.insertaIdF(node.getId(), node);
			tabla.abreBloqueV();
		} catch (IdentifyingIdException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override 
	public void postvisit(Funcion node) {
		tabla.cierraBloqueV();
	}

	@Override
	public void previsit(Programa node) {
		tabla.abreBloqueV();
	}

	@Override
	public void postvisit(Programa node) {
		tabla.cierraBloqueV();
	}

	@Override 
	public void visit(Identificador node) {
		try {
			Declaracion ref = tabla.buscaIdV(node.id());
			node.setRef(ref);
		} catch (IdentifyingIdException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (UnsuportedOperation e) {
			e.printStackTrace();
		}
	}

	@Override
	public void previsit(Call node) {
		try {
			Funcion ref = tabla.buscaIdF(node.functionID());
			node.setRef(ref);
		} catch (IdentifyingIdException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (UnsuportedOperation e) {
			e.printStackTrace();
		}
	}

	@Override public void previsit(Codigo node) {}
	@Override public void postvisit(Codigo node) {}
	@Override public void postvisit(Declaracion node) {}
	@Override public void previsit(ExpresionBinaria node) {}
	@Override public void previsit(ExpresionUnaria node) {}
	@Override public void previsit(ArrayWithKeys node) {}
	@Override public void previsit(AllTo node) {}
	@Override public void postvisit(ExpresionBinaria node) {}
	@Override public void postvisit(ExpresionUnaria node) {}
	@Override public void postvisit(ArrayWithKeys node) {}
	@Override public void postvisit(AllTo node) {}
	@Override public void visit(True node) {}
	@Override public void visit(False node) {}
	@Override public void visit(Number node) {}
	@Override public void previsit(Asignacion node) {}
	@Override public void previsit(Choose node) {}
	@Override public void previsit(IfThenElse node) {}
	@Override public void previsit(While node) {}
	@Override public void postvisit(Asignacion node) {}
	@Override public void postvisit(Call node) {}
	@Override public void postvisit(Choose node) {}
	@Override public void postvisit(IfThenElse node) {}
	@Override public void postvisit(While node) {}
	@Override public void visit(String id) {}
	@Override public void visit(int key) {}
	@Override public void previsit(ArrayOf arrayOf) {}
	@Override public void postvisit(ArrayOf arrayOf) {}
	@Override public void visit(Bool bool) {}
	@Override public void visit(Int int1) {}
}
