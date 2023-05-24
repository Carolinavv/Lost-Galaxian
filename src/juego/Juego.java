package juego;


import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego{

	private Entorno entorno;
	private AstroMegaShip astroAMegaShip;
	
	
	Juego()
	{

		this.entorno = new Entorno(this, "Lost Galaxian - Grupo 15 - v1", 800, 600);
		//ancho, x, y, alto, vel
		this.astroAMegaShip = new AstroMegaShip(20, 400, 550, 20,3);
		
		
		this.entorno.iniciar();
	}

	
	public void tick()
	{
		// Procesamiento de un instante de tiempo
		this.astroAMegaShip.dibujarse(entorno);
		
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
