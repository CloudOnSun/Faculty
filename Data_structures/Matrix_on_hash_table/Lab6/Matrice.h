#pragma once

#include <utility>

using namespace std;

typedef int TElem;

typedef pair<int, int > pereche;

#define NULL_TELEMENT 0

#define LUNG_TD 13

class Nod;

typedef Nod* PNod;


class Nod
{
private:
	TElem e;
	int col;
	int lin;
	PNod urm;
public:
	friend class Matrice;
	friend class Iterator;
	
	Nod(TElem e, PNod urm, int lin, int col) : e{ e }, urm{ urm }, lin{ lin }, col{ col } {}
	 
};

class Matrice {

private:
	int nrL;
	int nrC;
	int m;
	PNod td[LUNG_TD];

	int disp(TElem e) const;

public:
	friend class Iterator;

	//constructor
	//se arunca exceptie daca nrLinii<=0 sau nrColoane<=0
	Matrice(int nrLinii, int nrColoane);


	//destructor
	~Matrice();

	//returnare element de pe o linie si o coloana
	//se arunca exceptie daca (i,j) nu e pozitie valida in Matrice
	//indicii se considera incepand de la 0
	TElem element(int i, int j) const;

	pereche positionOf(TElem element) const;


	// returnare numar linii
	int nrLinii() const;

	// returnare numar coloane
	int nrColoane() const;


	// modificare element de pe o linie si o coloana si returnarea vechii valori
	// se arunca exceptie daca (i,j) nu e o pozitie valida in Matrice
	TElem modifica(int i, int j, TElem);

	Iterator iterator();

};

class Iterator {
private:
	//pentru a construi un iterator pe o colectie este necesar ca un pointer sau referinta la aceasta sa ii fie oferit constructorului
	Iterator(const Matrice& mat);

	void deplasare();

	//contine o referinta catre colectia pe care o itereaza

	const Matrice& mat;
	//locatia curenta din tabela
	int poz;
	//retine pozitia curenta in interiorul listei de la locatia curenta - adresa Nodului curent din lista asociata
	PNod curent;

public:

	friend class Matrice;

	//reseteaza pozitia iteratorului la inceputul colectiei
	void prim();

	//muta iteratorul pe urmatoarea pozitie in cadrul colectiei
	void urmator();

	//verifica daca iteratorul e valid (indica un element al colectiei
	bool valid() const;

	//returneaza valoarea curenta a elementului din colectie
	TElem element() const;
};






