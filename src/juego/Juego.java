package juego;


import entorno.Entorno;
import java.awt.Font;
import entorno.InterfaceJuego;
import java.awt.Color;
import java.util.Random;

public class Juego extends InterfaceJuego{

	private Entorno entorno;
	private AstroMegaShip astroAMegaShip;
	private DestructorEstelar destructorEstelar;
	Random rand = new Random();
	//ubicaciones x e y para destructores
    int xRand = rand.nextInt(790);
    int yRand = rand.nextInt(400);
	
	
	Juego()
	{
		Random rand = new Random();

		this.entorno = new Entorno(this, "Lost Galaxian - Grupo 15 - v1", 800, 600);
		//ancho, x, y, alto, vel
		this.astroAMegaShip = new AstroMegaShip(20, 400, 550, 20,3);
        
		this.destructorEstelar = new DestructorEstelar(xRand, yRand,10, 10, 2);
	
	
        	
		this.entorno.iniciar();
	}

	
	public void tick()
	{
		// Procesamiento de un instante de tiempo
		this.entorno.cambiarFont(Font.SANS_SERIF, 20, Color.WHITE);
		this.entorno.escribirTexto("Eliminados:", 650, 590);
		this.astroAMegaShip.dibujarse(entorno);
		this.destructorEstelar.dibujarse(entorno);
		
		

		
		if(this.entorno.estaPresionada(this.entorno.TECLA_DERECHA) && this.astroAMegaShip.getX() + this.astroAMegaShip.getAncho() / 2 < this.entorno.ancho()) {
			this.astroAMegaShip.moverDerecha();
		}
		
		if(this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)&& this.astroAMegaShip.getX() - this.astroAMegaShip.getAncho() / 2 > 0) {
			this.astroAMegaShip.moverIzquierda();
		}

	}
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
