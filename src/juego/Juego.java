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
	private Proyectil proyectil;
	Random rand = new Random();
	//ubicaciones x e y para destructores
    boolean destructores = false;
    int destructoresEliminados = 0;
    boolean juegoPerdido;
    boolean estaDisparando = false;
	
	
	Juego()
	{
		Random rand = new Random();
		juegoPerdido = false;
		this.entorno = new Entorno(this, "Lost Galaxian - Grupo 15 - v1", 800, 600);
		//ancho, x, y, alto, vel
        this.destructorEstelar = new DestructorEstelar[2];
        generarDestructoresEstelares();
        this.astroAMegaShip = new AstroMegaShip(20, 400, 520, 20,3);			
        
		this.entorno.iniciar();
	}

	
	public void tick()
	{
		if(!juegoPerdido) {
			this.entorno.cambiarFont(Font.SANS_SERIF, 20, Color.WHITE);
			this.entorno.escribirTexto("Eliminados: "+ destructoresEliminados, 650, 590);
			this.astroAMegaShip.dibujarse(entorno);
			
			if(estaDisparando == false) {
				if(this.entorno.sePresiono('e')) {
					proyectil = this.astroAMegaShip.disparar();
					
					estaDisparando = true;
				}
				
			}
			movimientoproyectil();
			if(this.proyectil!= null ) {
				if(this.proyectil.getY() >= 600 || this.proyectil.getY() <= 0) {
					this.proyectil = null;
					estaDisparando = false;
				}				
			}
			
			
			if(!colisionBordeDer()) {
				if(this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)|| this.entorno.estaPresionada('d') && this.astroAMegaShip.getX() + this.astroAMegaShip.getAncho() / 2 < this.entorno.ancho()) {
					this.astroAMegaShip.moverDerecha();
				}
			}
			if(!colisionBordeIzq()) {
				if(this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)|| this.entorno.estaPresionada('a')&& this.astroAMegaShip.getX() - this.astroAMegaShip.getAncho() / 2 > 0) {
					this.astroAMegaShip.moverIzquierda();
				}
			}
			
//			if(colisionNaveEnemigo()) {
//				System.out.println("colsionm");
//				juegoPerdido = true;
//			}
			
			if( this.destructorEstelar != null ) {
				dibujarDestructoresEstelares();
				moverDestructoresEstelares();
				
				System.out.println(this.destructorEstelar);
				for (int i = 0; i < destructorEstelar.length; i++) {
					if(this.destructorEstelar[i].getY() >= 600 || this.destructorEstelar[i].getY() <= 0) {
						//this.destructorEstelar[i] = null; ACA BORRAR DESTRUCTOR
						destructoresEliminados += 1;
					}
					
				}
			}
			
			
			
			
			
		}	
		
		if(juegoPerdido) {
			this.entorno.cambiarFont(Font.SANS_SERIF, 6, Color.WHITE);
			this.entorno.escribirTexto("PERDISTE", 200, 300);
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
		for (int i = 0; i < destructorEstelar.length ; i++) {
			this.destructorEstelar[i].dibujarse(entorno);			
		}
	}
	
	private void moverDestructoresEstelares() {
		for (int i = 0; i < destructorEstelar.length; i++) {
			this.destructorEstelar[i].moverAbajo();			
		}
	}
	
	private boolean colisionBordeDer() {
		return((this.astroAMegaShip.getX() + (this.astroAMegaShip.getAncho() / 2) > this.entorno.ancho()) );
		
	}
	
	private boolean colisionBordeIzq() {
		return( this.astroAMegaShip.getX() <= 0 );
		
	}
	
	private boolean colisionNaveEnemigo() {
		for (int i = 0; i < destructorEstelar.length; i++) {
			if((this.astroAMegaShip.getY() == this.destructorEstelar[i].getY()) || (this.astroAMegaShip.getX() == this.destructorEstelar[i].getY())) {
				return true;
			}
					
		}
		return false;
		
	}
	
	public void movimientoproyectil() {
		if(proyectil != null) {
			if(this.proyectil.avanzar()) {
				this.proyectil.dibujarse(this.entorno);
			}else {
				proyectil = null;
			}
		}
	}
	
	public void borrarDisparo(Proyectil disparo) {
		if(disparo.getX() > 800 || disparo.getX() < 0) {
			disparo = null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
