package juego;


import entorno.Entorno;
import java.awt.Font;
import entorno.InterfaceJuego;
import java.awt.Color;
import java.util.Iterator;
import java.util.Random;

public class Juego extends InterfaceJuego{

	private Entorno entorno;
	private AstroMegaShip astroAMegaShip;
	private DestructorEstelar[] destructorEstelar;
	Random rand = new Random();
	//ubicaciones x e y para destructores
    boolean destructores = false;
	
	
	Juego()
	{
		Random rand = new Random();

		this.entorno = new Entorno(this, "Lost Galaxian - Grupo 15 - v1", 800, 600);
		//ancho, x, y, alto, vel
		this.astroAMegaShip = new AstroMegaShip(20, 400, 550, 20,3);
        this.destructorEstelar = new DestructorEstelar[5];
        generarDestructoresEstelares();
		
        	
		this.entorno.iniciar();
	}

	
	public void tick()
	{
		// Procesamiento de un instante de tiempo
		this.entorno.cambiarFont(Font.SANS_SERIF, 20, Color.WHITE);
		this.entorno.escribirTexto("Eliminados:", 650, 590);
		this.astroAMegaShip.dibujarse(entorno);
		dibujarDestructoresEstelares();
		
		

	
		if(this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)|| this.entorno.estaPresionada('d') && this.astroAMegaShip.getX() + this.astroAMegaShip.getAncho() / 2 < this.entorno.ancho()) {
			this.astroAMegaShip.moverDerecha();
		}
		
		if(this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)|| this.entorno.estaPresionada('a')&& this.astroAMegaShip.getX() - this.astroAMegaShip.getAncho() / 2 > 0) {
			this.astroAMegaShip.moverIzquierda();
		}

	}
	
	private void generarDestructoresEstelares() {
		for (int i = 0; i < destructorEstelar.length; i++) {
			 int xRand = rand.nextInt(790);
			 int yRand = rand.nextInt(400);
			 this.destructorEstelar[i] = new DestructorEstelar(xRand, yRand,10, 10, 2);
		}
	}
	
	private void dibujarDestructoresEstelares() {
		for (int i = 0; i < destructorEstelar.length; i++) {
			this.destructorEstelar[i].dibujarse(entorno);			
		}
	}
	
	
	
	
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
