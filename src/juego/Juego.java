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
	private Proyectil proyectil;
	Random rand = new Random();
	//ubicaciones x e y para destructores
    boolean destructores = false;
    int destructoresEliminados = 0;
    boolean juegoPerdido;
    boolean estaDisparando = false;
    public Image ImagenDestructor = Herramientas.cargarImagen("Imagenes/DestructorEstelar.png");
    public Image fondo = Herramientas.cargarImagen("Imagenes/Fondo.png");
	public Image nave = Herramientas.cargarImagen("Imagenes/AstroMegaShip.png");
	public Image ImagenProyectil = Herramientas.cargarImagen("Imagenes/Proyectil.png");
	
	
	
	Juego()
	{
		this.entorno = new Entorno(this, "Lost Galaxian - Grupo 15 - v1", 800, 600);
		juegoPerdido = false;
		
		//ancho, x, y, alto, vel
        this.destructorEstelar = new DestructorEstelar[2];
        generarDestructoresEstelares(ImagenDestructor);
        this.astroAMegaShip = new AstroMegaShip(50, 400, 520, 55,3, nave);			
        this.entorno.dibujarImagen(fondo, 100, 100, 0);
		this.entorno.iniciar();
	}

	
	public void tick()
	{
		if(!juegoPerdido) {
			this.entorno.cambiarFont(Font.SANS_SERIF, 20, Color.WHITE);
			this.entorno.escribirTexto("Eliminados: "+ destructoresEliminados, 650, 590);
			this.astroAMegaShip.dibujarse(entorno);
			dibujarDestructoresEstelares();
			moverDestructoresEstelares();
			//System.out.println("astro: x:"+ this.astroAMegaShip.getX()+ ", y:"+ this.astroAMegaShip.getY());
			
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
				if(colisionProyectilEnemigo()) {
					destructoresEliminados += 1;
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
			
			if(colisionNaveEnemigo()) {
				System.out.println("colsionm");
				juegoPerdido = true;
			}
			
			
			
			if( this.destructorEstelar != null ) {
				for (int i = 0; i < destructorEstelar.length; i++) {
					if(this.destructorEstelar[i] != null) {
						//System.out.println("enemigo: x:"+ this.destructorEstelar[i].getX()+ ", y:"+ this.destructorEstelar[i].getY());
						
						
						if(this.destructorEstelar[i].getY() >= 600 || this.destructorEstelar[i].getY() <= 0) {
							this.destructorEstelar[i] = null; 
							destructoresEliminados += 1;
							generarNuevoDestructorEstelar(i, ImagenDestructor);
						}						
					}else {
						generarNuevoDestructorEstelar(i, ImagenDestructor);
					}
					
				}
			}
			
			
			
			
			
		}	
		
		if(juegoPerdido) {
			this.entorno.cambiarFont(Font.SANS_SERIF, 30, Color.WHITE);
			this.entorno.escribirTexto("PERDISTE", 320, 300);
		}
		//fin Tick
	}
	public int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	private void generarDestructoresEstelares(Image ImagenDestructor) {
		for (int i = 0; i < destructorEstelar.length; i++) {
				int xRand = getRandomNumber(50, 780);
				int yRand = getRandomNumber(50, 200);
				this.destructorEstelar[i] = new DestructorEstelar(xRand, yRand,20, 20, 2, ImagenDestructor);				
			
		}
	}
	
	private void generarNuevoDestructorEstelar(int posicion, Image ImagenDestructor) {
		int xRand = getRandomNumber(50, 780);
		int yRand = getRandomNumber(50, 200);
		this.destructorEstelar[posicion] = new DestructorEstelar(xRand, yRand, 50, 50, 2, ImagenDestructor);	
	}
	
	private void dibujarDestructoresEstelares() {
		for (int i = 0; i < destructorEstelar.length ; i++) {
			if(this.destructorEstelar[i] != null) {
				this.destructorEstelar[i].dibujarse(entorno);							
			}else {
				System.out.println(i+"es nulo");
			}
		}
	}
	
	private void moverDestructoresEstelares() {
		for (int i = 0; i < destructorEstelar.length; i++) {
			if(this.destructorEstelar[i] != null) {
				this.destructorEstelar[i].moverAbajo();							
			}else {
				System.out.println(i+"es nulo");
			}
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
					System.out.println("colisiona");
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
