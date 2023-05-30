package juego;
import java.awt.Image;
import entorno.Entorno;
import java.awt.Color;

public class Asteroide {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private int velocidad;
	private Image imagenAsteroide;
	private int direccion;
	
	
	public Asteroide(int x, int y, int ancho, int alto, int velocidad, int direccion, Image imagenAsteroide) {
		super();
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = velocidad;
		this.direccion = direccion;
		this.imagenAsteroide = imagenAsteroide;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getAncho() {
		return ancho;
	}
	
	public void setAncho(int ancho) {
		this.ancho = ancho;
	}
	
	public int getAlto() {
		return alto;
	}
	
	public void setAlto(int alto) {
		this.alto = alto;
	}
	
	public int getVelocidad() {
		return velocidad;
	}
	
	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}
	
	
	public int getDireccion() {
		return direccion;
	}

	public void setDireccion(int direccion) {
		this.direccion = direccion;
	}

	void moverDerecha() {
		this.y = this.y + 1;
		this.x = this.x + 1;
	}
	
	void moverIzquierda() {
		this.y = this.y + 1;
		this.x = this.x - 1;
	}
	
	void dibujarse(Entorno entorno) {
		entorno.dibujarImagen(this.imagenAsteroide, this.x, this.y, 0);
		//entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.RED);
	}
	
	
}
