package juego;

import java.awt.Color;
import java.util.Random;

import entorno.Entorno;

public class DestructorEstelar {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private int velocidad;

	public DestructorEstelar(int x, int y, int ancho, int alto, int velocidad) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = velocidad;
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
	void moverDerecha () {
		this.x = this.x + this.velocidad;	
		this.y = this.y + 1;
	}
		
	void moverIzquierda(){
		this.x = this.x - this.velocidad;
		this.y = this.y + 1;
	}
	
	
	
	void dibujarse(Entorno entorno) {
		entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.RED);
	}
	
	
	
}
