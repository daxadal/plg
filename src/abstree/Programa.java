package src.abstree;

import java.util.LinkedList;

import src.errors.UnsuportedOperation;
import src.resolid.Anfitrion;
import src.resolid.Visitante;
import src.abstree.sentencias.Sentencia;

public class Programa implements Anfitrion{
	
	public Programa(LinkedList<Declaracion> declaraciones, LinkedList<Sentencia> sentencias){
		this.declaraciones = declaraciones;
		this.sentencias = sentencias;
	}
	
	@Override
	public void accept(Visitante v) {
		v.previsit(this);
		for(Declaracion d: declaraciones)
			d.accept(v);
		for(Sentencia s: sentencias)
			s.accept(v);
		v.postvisit(this);		
	}


	public boolean checkTipo() throws UnsuportedOperation {
		LinkedList<Declaracion> decs = declaraciones;
		for(int i=1;i<=decs.size();i++) {
			if(!decs.get(i).checkTipo())
				throw new UnsuportedOperation("Error en los tipos de las declaraciones del programa.");
		}
		LinkedList<Sentencia> sents = sentencias;
		for(int i=1;i<=sents.size();i++) {
			if(!sents.get(i).checkTipo())
				throw new UnsuportedOperation("Error en los tipos de las sentencias del programa.");
		}
		
		return true;
	}
	
	private LinkedList<Declaracion> declaraciones;
	private LinkedList<Sentencia> sentencias;
}
