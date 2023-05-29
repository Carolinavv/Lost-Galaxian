package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;

public class Proyectil {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private int velocidad;
	private Image ImagenProyectil;
		
	public Proyectil(int x, int y, int ancho, int alto, int velocidad) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = velocidad;
		this.ImagenProyectil = entorno.Herramientas.cargarImagen("Imagenes/Proyectil.png");
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
	
	public boolean avanzar() {
		 this.y = this.y - this.velocidad;
		 return true;
	}
	
	void dibujarse(Entorno entorno) {
		//entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.GREEN);
		entorno.dibujarImagen(ImagenProyectil, this.x, this.y, 0);
	}
	
}























