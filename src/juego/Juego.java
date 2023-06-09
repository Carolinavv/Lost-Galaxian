package juego;


import entorno.Entorno;
import java.awt.Font;
import entorno.InterfaceJuego;
import java.awt.Color;
import java.util.Arrays;
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
	private Proyectil[] proyectilEnemigo;
	
    boolean destructores = false;
    int destructoresEliminados = 0;
    boolean juegoPerdido;
    boolean estaDisparando = false;
    boolean enemigoEstaDisparando[];
    int posicionesEnemigos[];
    int nuevasPosicionesEnemigos[];
    boolean flag = true;
    public Image ImagenDestructor = Herramientas.cargarImagen("Imagenes/DestructorEstelar.png");
    public Image fondo = Herramientas.cargarImagen("Imagenes/Fondo.png");
	public Image nave = Herramientas.cargarImagen("Imagenes/AstroMegaShip.png");
	public Image ImagenProyectil = Herramientas.cargarImagen("Imagenes/Proyectil.png");
	public Image ImagenAsteroideIZQ = Herramientas.cargarImagen("Imagenes/AsteroideIZQ.png");
	public Image ImagenAsteroideDER = Herramientas.cargarImagen("Imagenes/AsteroideDER.png");
	
	int amplitud = 50 ; // Amplitud de la oscilación
	double frecuencia = 0.3 ; // Frecuencia de la oscilación
	double velocidad = 0.1; 
	int posicion_inicial = 0;

	long tiempoInicial = System.currentTimeMillis();
	
	Juego()
	{
		this.entorno = new Entorno(this, "Lost Galaxian - Grupo 15 - v1", 800, 600);
		juegoPerdido = false;
		this.astroAMegaShip = new AstroMegaShip(50, 400, 520, 55,3, nave);	
        this.destructorEstelar = new DestructorEstelar[5];
        this.proyectilEnemigo = new Proyectil[5];
        enemigoEstaDisparando = new boolean[5];
        this.asteroides = new Asteroide[4];
        generarAsteroides();
		this.entorno.iniciar();
		posicionesEnemigos = generaPosiciones();
		generarDestructoresEstelares(ImagenDestructor);
	}

	
	public void tick()
	{
		this.entorno.dibujarImagen(fondo, 400, 300, 0);
		this.entorno.dibujarRectangulo(0, 550, 5, 5, 0, Color.RED);
		
		
		if(!juegoPerdido && flag ) {
			

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
			
			//manejo proyectil de los destructores estelares
			for(int k = 0 ; k < proyectilEnemigo.length; k++ ) {
				//Solo dispara la nave 1 y 3 (por temas de dificultad)
				if(k == 1 || k == 3) {
					
					if(this.destructorEstelar[k] != null) {
						if(enemigoEstaDisparando[k] == false){
							//si el enemigo no esta disparando va a disparar
							proyectilEnemigo[k] = this.destructorEstelar[k].disparar();
							enemigoEstaDisparando[k] = true;
						}
						movimientoproyectilEnemigo(k);
						
						if(this.proyectilEnemigo[k] != null) {
							
							
							//si pasa el borde de pantalla se borra
							if(this.proyectilEnemigo[k].getY() >= 600) {
								this.proyectilEnemigo[k] = null;
								enemigoEstaDisparando[k] = false;
							}
							
							if(colisionProyectilDelEnemigo()) {
								this.astroAMegaShip = null;
								this.proyectilEnemigo[k] = null;
								enemigoEstaDisparando[k] = false;
								juegoPerdido = true;
							}
						}
					}
				}
				
				
			}
			
			
			
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
			if(astroAMegaShip != null) {
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
				
			}
			
			
			
			 long tiempoActual = System.currentTimeMillis();

	         // Calcular el tiempo transcurrido en segundos
	         double tiempoTranscurrido = (tiempoActual - tiempoInicial) / 1000.0;

			if( this.destructorEstelar != null ) {
				for (int i = 0; i < destructorEstelar.length; i++) {
					
					
					if(this.destructorEstelar[i] != null) {
						//oscilacion
						int value = (int) ( amplitud * Math.sin(2 * Math.PI * frecuencia * tiempoTranscurrido) + velocidad * tiempoTranscurrido + posicionesEnemigos[i]);
						destructorEstelar[i].setX(value);
						
						//Borra el enemigo al llegar al borde inferior
						if(this.destructorEstelar[i].getY() >= 600 || this.destructorEstelar[i].getY() < -100) {
							this.destructorEstelar[i] = null;
						}						
					}else {
						//genera un nuevo enemigo en la posicion del arreglo del que fue borrado
						generarNuevoDestructorEstelar(i, ImagenDestructor);
					}
					
				}
			}
				
		}
		
		if(destructoresEliminados >= 10) {
			this.entorno.cambiarFont(Font.SANS_SERIF, 30, Color.WHITE);
			this.entorno.escribirTexto("GANASTE", 320, 300);
			this.entorno.cambiarFont(Font.SANS_SERIF, 20, Color.WHITE);
			this.entorno.escribirTexto("Puntuacion: "+ destructoresEliminados, 330, 350);
			this.astroAMegaShip = null;
			this.asteroides = null;
			this.destructorEstelar = null;
			proyectilEnemigo = null;
			flag = false;
			
		}
		
		if(juegoPerdido && flag) {
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
	
	private void generarDestructoresEstelares(Image ImagenDestructor) {
		for(int i = 0; i < posicionesEnemigos.length ; i++) {
			this.destructorEstelar[i] = new DestructorEstelar(posicionesEnemigos[i], 0, 50, 55, 2, ImagenDestructor);	
		}
	}
	
	public static int[] generaPosiciones() {
	        int[] positions = new int[5];
	        Random random = new Random();

	        for (int i = 0; i < 5; i++) {
	            int position = random.nextInt(651) + 80; 

	            // Verifica que la posición generada cumpla con los requisitos
	            boolean valid = true;
	            for (int j = 0; j < i; j++) {
	                if (Math.abs(position - positions[j]) < 80) {
	                    valid = false;
	                    break;
	                }
	            }

	            if (valid) {
	                positions[i] = position;
	            } else {
	                // Si la posición generada no cumple, genera otra posición
	                i--;
	            }
	        }

	        return positions;
	    }
	
	public static int[] generaNuevaPosicion(int pos, int[]positions) {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int position = random.nextInt(651) + 80;

            // Verifica que la posición generada cumpla con los requisitos
            boolean valid = true;
            for (int j = 0; j < i; j++) {
                if (Math.abs(position - positions[j]) < 80) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                positions[pos] = position;
            } else {
                // Si la posición generada no cumple los requisitos, genera otra posición
                i--;
            }
        }
   
        return positions;
    }
	
	
	private void generarNuevoDestructorEstelar(int posicion, Image ImagenDestructor) {
		//genero una nueva posicion 
		posicionesEnemigos = generaNuevaPosicion(posicion, posicionesEnemigos);
		this.destructorEstelar[posicion] = new DestructorEstelar(posicionesEnemigos[posicion], -50, 50, 55, 2, ImagenDestructor);	
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
	
	
	public boolean colisionProyectilDelEnemigo() {
		for (int i = 0; i < proyectilEnemigo.length; i++) {
			if(astroAMegaShip != null && this.proyectilEnemigo[i] != null) {
				if(
						(this.proyectilEnemigo[i].getY() >=  this.astroAMegaShip.getY()-(this.astroAMegaShip.getAlto()/2) && this.proyectilEnemigo[i].getY() <=  this.astroAMegaShip.getY()+(this.astroAMegaShip.getAlto()/2)) &&
						(this.proyectilEnemigo[i].getX() >=  this.astroAMegaShip.getX()-(this.astroAMegaShip.getAncho()/2) && this.proyectilEnemigo[i].getX() <=  this.astroAMegaShip.getX()+(this.astroAMegaShip.getAncho()/2))) {
					
					this.astroAMegaShip = null;
					return true;
					
				}
							
			}
		}
		return false;
	}
	
	
	public void movimientoproyectilEnemigo(int pos) {
		if(proyectilEnemigo[pos] != null) {
			if(this.proyectilEnemigo[pos].avanzarAbajo()){
				this.proyectilEnemigo[pos].dibujarse(this.entorno);
			}else {
				proyectilEnemigo[pos] = null;
			}
		}
	}
	
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
