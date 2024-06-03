package org.formation.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;

public class OutputProduct {

	public OutputProduct() {
		// TODO Auto-generated constructor stub
	}
	public OutputProduct(InputProduct inputProduct) {
		this.hauteur = inputProduct.getHauteur();
		this.largeur = inputProduct.getLargeur();
		this.longueur = inputProduct.getLongueur();
		this.reference = inputProduct.getReference();
		this.nom = inputProduct.getNom();
				
		instant = Instant.now();
	}
	@NotEmpty
	@Length(min = 5, max = 5)
	private String reference;
	@NotEmpty
	private String nom;
	
	private Float hauteur, largeur, longueur;
	@NotNull
	private Instant instant;
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Float getHauteur() {
		return hauteur;
	}
	public void setHauteur(Float hauteur) {
		this.hauteur = hauteur;
	}
	public Float getLargeur() {
		return largeur;
	}
	public void setLargeur(Float largeur) {
		this.largeur = largeur;
	}
	public Float getLongueur() {
		return longueur;
	}
	public void setLongueur(Float longueur) {
		this.longueur = longueur;
	}
	public Instant getInstant() {
		return instant;
	}
	public void setInstant(Instant instant) {
		this.instant = instant;
	}
	
}
