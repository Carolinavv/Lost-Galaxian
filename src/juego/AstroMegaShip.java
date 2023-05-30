package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Herramientas;
import entorno.Entorno;

public class AstroMegaShip {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private int velocidad;
	private Image ImagenAstro;
	
	AstroMegaShip(int ancho, int x, int y, int alto, int velocidad, Image ImagenAstro) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = velocidad;
		this.ImagenAstro = ImagenAstro;
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

	void moverDerecha () {
		this.x = this.x + this.velocidad;	
	}
		
	void moverIzquierda(){
		this.x = this.x - this.velocidad;	
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
	
	public Proyectil disparar() {
		return new Proyectil(this.x, this.y, 4, 4, 20);
	}
	

	void dibujarse(Entorno entorno) {
		entorno.dibujarImagen(ImagenAstro, this.x, this.y, 0);
	}
}
