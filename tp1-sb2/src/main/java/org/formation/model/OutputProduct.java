package org.formation.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "produit")
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

		instant = new Date();
	}


	private String reference;

	private String nom;

	private Float hauteur, largeur, longueur;

	private Date instant;

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

	@XmlTransient
	public Date getInstant() {
		return instant;
	}

	public void setInstant(Date instant) {
		this.instant = instant;
	}	

	@XmlElement(name = "importedDate")
	public String getImportedDate() {
		return instant.toString();
	}



}
