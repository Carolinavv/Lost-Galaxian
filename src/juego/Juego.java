package juego;


import entorno.Entorno;
import java.awt.Font;
import entorno.InterfaceJuego;
import java.awt.Color;
import java.util.Iterator;
import java.util.Random;
import java.awt.Image;
import entorno.Herramientas;

public class Juego extends InterfaceJuego{

	private Entorno entorno;
	private AstroMegaShip astroAMegaShip;
	private DestructorEstelar[] destructorEstelar;
	private Asteroide[] asteroides;
	private Proyectil proyectil;
	
    boolean destructores = false;
    int destructoresEliminados = 0;
    boolean juegoPerdido;
    boolean estaDisparando = false;
    public Image ImagenDestructor = Herramientas.cargarImagen("Imagenes/DestructorEstelar.png");
    public Image fondo = Herramientas.cargarImagen("Imagenes/Fondo.png");
	public Image nave = Herramientas.cargarImagen("Imagenes/AstroMegaShip.png");
	public Image ImagenProyectil = Herramientas.cargarImagen("Imagenes/Proyectil.png");
	public Image ImagenAsteroideIZQ = Herramientas.cargarImagen("Imagenes/AsteroideIZQ.png");
	public Image ImagenAsteroideDER = Herramientas.cargarImagen("Imagenes/AsteroideDER.png");
	
	
	
	Juego()
	{
		this.entorno = new Entorno(this, "Lost Galaxian - Grupo 15 - v1", 800, 600);
		juegoPerdido = false;
		this.astroAMegaShip = new AstroMegaShip(50, 400, 520, 55,3, nave);	
        this.destructorEstelar = new DestructorEstelar[5];
        generarDestructoresEstelares(ImagenDestructor);
        this.asteroides = new Asteroide[4];
        generarAsteroides();
		this.entorno.iniciar();
	}

	
	public void tick()
	{
		this.entorno.dibujarImagen(fondo, 400, 300, 0);
		if(!juegoPerdido) {
			this.entorno.cambiarFont(Font.SANS_SERIF, 20, Color.WHITE);
			this.entorno.escribirTexto("Eliminados: "+ destructoresEliminados, 650, 590);
			this.astroAMegaShip.dibujarse(entorno);
			dibujarDestructoresEstelares();
			dibujarAsteroides();
			moverDestructoresEstelares();
			moverAsteroides();
						
			if(estaDisparando == false) {
				if(this.entorno.sePresiono('e') || this.entorno.sePresiono(entorno.TECLA_ESPACIO)) {
					proyectil = this.astroAMegaShip.disparar();
					estaDisparando = true;
				}
			}
			movimientoproyectil();
			
			if(this.proyectil!= null ) {
				//Borra el proyectil al llegar a el borde superior
				if(this.proyectil.getY() >= 600 || this.proyectil.getY() <= 0) {
					this.proyectil = null;
					estaDisparando = false;
				}
				//Maneja colisiones proyectil - enemigo
				if(colisionProyectilEnemigo()) {
					destructoresEliminados += 1;
					this.proyectil = null;
					estaDisparando = false;
				}	
			}
			
			//Evita que astroMegaShip no salga de la pantalla
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
		
			//Manejo colision Astromegaship - enemigo
			if(colisionNaveEnemigo()) {
				juegoPerdido = true;
			}
			
			if(this.asteroides != null) {
				//colision asteroides - AstromegaShip
				if(colisionAsteroideConAstroMegaShip()) {
					juegoPerdido = true;
				}
				
				for (int i = 0; i < asteroides.length; i++) {
				//Genera nuevos asteroides cuando estos salen de la pantalla
					if(this.asteroides[i].getY() >= 600 || this.asteroides[i].getY() <= 0 || this.asteroides[i].getX() > 800 || this.asteroides[i].getX() < 0 ) {
							this.asteroides[i] = null;
							generarNuevosAsteroides(i);
					}
				}
			}
			
			
			if( this.destructorEstelar != null ) {
				for (int i = 0; i < destructorEstelar.length; i++) {
//					colisionAsteroideDestructorEstelar();
					if(this.destructorEstelar[i] != null) {
						//Borra el enemigo al llegar al borde inferior
						if(this.destructorEstelar[i].getY() >= 600 || this.destructorEstelar[i].getY() < -100) {
							this.destructorEstelar[i] = null;
							//genera un nuevo enemigo en la posicion del arreglo del que fue borrado
							generarNuevoDestructorEstelar(i, ImagenDestructor);
						}						
					}else {
						//genera un nuevo enemigo en la posicion del arreglo del que fue borrado
						generarNuevoDestructorEstelar(i, ImagenDestructor);
					}
					
				}
			}
				
		}	
		
		if(juegoPerdido) {
			this.entorno.cambiarFont(Font.SANS_SERIF, 30, Color.WHITE);
			this.entorno.escribirTexto("PERDISTE", 320, 300);
			this.entorno.cambiarFont(Font.SANS_SERIF, 20, Color.WHITE);
			this.entorno.escribirTexto("Puntuacion: "+ destructoresEliminados, 330, 350);
			this.astroAMegaShip = null;
			this.asteroides = null;
			this.destructorEstelar = null;
		}
		//fin Tick
	}
	
	public int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	private void generarDestructoresEstelares(Image ImagenDestructor) {
		for (int i = 0; i < destructorEstelar.length; i++) {
			int xRand = getRandomNumber(50, 780);
			int yRand = 0;
			int xRand2;
			if(i>=1) {
				do {
					xRand2 = getRandomNumber(50, 780);
				}while(this.destructorEstelar[i-1].getX()+80 < xRand2 && this.destructorEstelar[i-1].getX()-80 > xRand2 );	
				this.destructorEstelar[i] = new DestructorEstelar(xRand2, yRand,50, 55, 2, ImagenDestructor);
				return;
			}else {
				this.destructorEstelar[i] = new DestructorEstelar(xRand, yRand,50, 55, 2, ImagenDestructor);								
			}
		}
	}
	
	
	private void generarAsteroides() {
		for (int i = 0; i < asteroides.length; i++) {
			int flagDireccion = getRandomNumber(1,3);
			int xRand = getRandomNumber(50, 780);
			int yRand = 0;
			if(flagDireccion == 1) {
				this.asteroides[i] = new Asteroide(xRand, yRand,25, 23, 3, flagDireccion, ImagenAsteroideDER);					
			}else {
				this.asteroides[i] = new Asteroide(xRand, yRand,25, 23, 3, flagDireccion, ImagenAsteroideIZQ);
			}		
		}
	}
	
	private void generarNuevosAsteroides(int posicion) {
		int flagDireccion = getRandomNumber(1,3);
		int xRand = getRandomNumber(50, 780);
		int yRand = 0;
		if(flagDireccion == 1) {
			this.asteroides[posicion] = new Asteroide(xRand, yRand,25, 23, 3, flagDireccion, ImagenAsteroideDER);					
		}else {
			this.asteroides[posicion] = new Asteroide(xRand, yRand,25, 23, 3, flagDireccion, ImagenAsteroideIZQ);
		}
	}
	
	private void dibujarAsteroides() {
		for (int i = 0; i < asteroides.length ; i++) {
			if(this.asteroides[i] != null) {
				this.asteroides[i].dibujarse(entorno);							
			}
		}
	}
	
	private void moverAsteroides() {
		for (int i = 0; i < asteroides.length; i++) {
			if(this.asteroides[i] != null) {
				if(this.asteroides[i].getDireccion() == 1) {
					this.asteroides[i].moverDerecha();
				}else {
					this.asteroides[i].moverIzquierda();
				}
			}
		}
	}
	
	
	private void generarNuevoDestructorEstelar(int posicion, Image ImagenDestructor) {
		int xRand = getRandomNumber(50, 780);
		int yRand = 0;
		this.destructorEstelar[posicion] = new DestructorEstelar(xRand, yRand, 50, 55, 2, ImagenDestructor);	
	}
	
	private void dibujarDestructoresEstelares() {
		for (int i = 0; i < destructorEstelar.length ; i++) {
			if(this.destructorEstelar[i] != null) {
				this.destructorEstelar[i].dibujarse(entorno);							
			}
		}
	}
	
	private void moverDestructoresEstelares() {
		for (int i = 0; i < destructorEstelar.length; i++) {
			if(this.destructorEstelar[i] != null) {
				this.destructorEstelar[i].moverAbajo();							
			}
		}
	}
	
	private boolean colisionAsteroideConAstroMegaShip() {
		for (int i = 0; i < asteroides.length ; i++) {
			if(
					(this.asteroides[i].getY() >=  this.astroAMegaShip.getY()-(this.astroAMegaShip.getAlto()/2) && this.asteroides[i].getY() <=  this.astroAMegaShip.getY()+(this.astroAMegaShip.getAlto()/2)) &&
					(this.asteroides[i].getX() >=  this.astroAMegaShip.getX()-(this.astroAMegaShip.getAncho()/2) && this.asteroides[i].getX() <=  this.astroAMegaShip.getX()+(this.astroAMegaShip.getAncho()/2))) {
				
				return true;
			}			
			
		}
		return false;
	}
	
//	private boolean colisionAsteroideDestructorEstelar() {
//		for (int i = 0; i < asteroides.length ; i++) {
//			for(int j = 0; j < destructorEstelar.length ; j++) {
//				if(asteroides[i] != null && destructorEstelar[j] != null) {
//					if(
//							//Si el asteroide se acerca a una destructorEstelar se mueve el destructorEstelar para evitar una colsion
//							(this.asteroides[i].getY()+30 >=  this.destructorEstelar[j].getY()-(this.destructorEstelar[j].getAlto()/2) && this.asteroides[i].getY() <=  this.destructorEstelar[j].getY()+(this.destructorEstelar[j].getAlto()/2)) &&
//							(this.asteroides[i].getX()+30 >=  this.destructorEstelar[j].getX()-(this.destructorEstelar[j].getAncho()/2) && this.asteroides[i].getX() <=  this.destructorEstelar[j].getX()+(this.destructorEstelar[j].getAncho()/2))) {
//						if(this.asteroides[i].getDireccion() == 1) {
//							//Mueve el destructor a la izquierda
//							this.destructorEstelar[j].setX(this.destructorEstelar[j].getX()-50);
//							this.destructorEstelar[j].setY(this.destructorEstelar[j].getY()-80);
//							return true;
//						}else {
//							//Mueve el destructor a la derecha
//							this.destructorEstelar[j].setX(this.destructorEstelar[j].getX()+50);
//							this.destructorEstelar[j].setY(this.destructorEstelar[j].getY()-80);
//							
//							return true;
//						}
//						
//						
//					}
//					
//				}
//				
//			}
//			
//		}
//		return false;
//	}

	
	private boolean colisionBordeDer() {
		return((this.astroAMegaShip.getX() + (this.astroAMegaShip.getAncho() / 2) > this.entorno.ancho()) );
		
	}
	
	private boolean colisionBordeIzq() {
		return( this.astroAMegaShip.getX() <= 0 );
		
	}
	
	private boolean colisionNaveEnemigo() {
		for (int i = 0; i < destructorEstelar.length; i++) {
			if(destructorEstelar[i] != null) {
			if(
					(this.destructorEstelar[i].getY() >=  this.astroAMegaShip.getY()-(this.astroAMegaShip.getAlto()/2) && this.destructorEstelar[i].getY() <=  this.astroAMegaShip.getY()+(this.astroAMegaShip.getAlto()/2)) &&
					(this.destructorEstelar[i].getX() >=  this.astroAMegaShip.getX()-(this.astroAMegaShip.getAncho()/2) && this.destructorEstelar[i].getX() <=  this.astroAMegaShip.getX()+(this.astroAMegaShip.getAncho()/2))) {
				return true;
			}
			}
		}
		return false;
		
	}
	
	public boolean colisionProyectilEnemigo() {
		for (int i = 0; i < destructorEstelar.length; i++) {
			if(destructorEstelar[i] != null && this.proyectil != null) {
				if(
						(this.proyectil.getY() >=  this.destructorEstelar[i].getY()-(this.destructorEstelar[i].getAlto()/2) && this.proyectil.getY() <=  this.destructorEstelar[i].getY()+(this.destructorEstelar[i].getAlto()/2)) &&
						(this.proyectil.getX() >=  this.destructorEstelar[i].getX()-(this.destructorEstelar[i].getAncho()/2) && this.proyectil.getX() <=  this.destructorEstelar[i].getX()+(this.destructorEstelar[i].getAncho()/2))) {
					this.destructorEstelar[i] = null;
					return true;
					
				}
							
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
	
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
