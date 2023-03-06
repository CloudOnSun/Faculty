#include "Dictionar.h"
#include <iostream>
#include "IteratorDictionar.h"
#include <limits.h>

int Dictionar::aloca()
{
	/*
	* CF=CD=CM=CG=TETA(1)
	*/
	int pozitie = primLiber;
	primLiber = urmator[primLiber];
	return pozitie;
}

void Dictionar::dealoca(int pozitie)
{
	/*
	* CF=CD=CM=CG=TETA(1)
	*/
	urmator[pozitie] = primLiber;
	//std::cout << primLiber << std::endl;
	if (primLiber != -1)
	{
		precedent[primLiber] = pozitie;
	}
	primLiber = pozitie;
}

void Dictionar::redimensionare()
{
	/*
	* CF=CD=CM=CG=TETA(N); N-NUMARUL DE ELEMENTE DIN DICTIONAR
	*/
	TElem* nouElemente = new TElem[2*capacitate];
	int* nouUrmator = new int[2*capacitate];
	int* nouPrecedent = new int[2*capacitate];
	for (int i = 0; i < capacitate; i++)
	{
		nouElemente[i] = elemente[i];
		nouUrmator[i] = urmator[i];
		nouPrecedent[i] = precedent[i];
	}
	//delete[] urmator
	for (int i = capacitate; i < 2*capacitate - 1; i++)
	{
		nouUrmator[i] = i + 1;
		nouPrecedent[i + 1] = i;
	}
	nouUrmator[2*capacitate-1] = -1;
	nouPrecedent[capacitate] = -1;
	primLiber = capacitate;
	capacitate = capacitate * 2;
	delete[] elemente;
	elemente = nouElemente;
	delete[] urmator;
	urmator = nouUrmator;
	delete[] precedent;
	precedent = nouPrecedent;
}

void Dictionar::initSpatiuLiber()
{
	/*
	* CF=CD=CM=CG=TETA(N); N-CAPACITATEA DICTIONARULUI
	* SE APELEAZA DOAR IN CONSTRUCTOR CAND N=5
	*/
	for (int i = 0; i < capacitate - 1; i++)
	{
		urmator[i] = i + 1;
		precedent[i + 1] = i;
	}
	urmator[capacitate - 1] = -1;
	precedent[0] = -1;
	primLiber = 0;
}

int Dictionar::creeazaNod(TElem element)
{
	/*
	* CF=CD=CM=CG=TETA(1) - AMORTIZAT
	*/
	if (primLiber == -1)
	{
		redimensionare();
	}
	int pozitie = aloca();
	elemente[pozitie] = element;
	urmator[pozitie] = -1;
	//std::cout << pozitie << std::endl;
	precedent[pozitie] = -1;
	return pozitie;
}

Dictionar::Dictionar() {
	/* de adaugat */
	/*
	* CF=CD=CM=CG=TETA(1)
	*/
	this->prim = -1;
	this->ultim = -1;
	this->capacitate = 5;
	this->elemente = new TElem[capacitate];
	this->urmator = new int[capacitate];
	this->precedent = new int[capacitate];
	initSpatiuLiber();
}

Dictionar::~Dictionar() {
	/* de adaugat */
	/*
	* CF=CD=CM=CG=TETA(1)
	*/
	delete[] elemente;
	delete[] urmator;
	delete[] precedent;
}

TValoare Dictionar::adauga(TCheie c, TValoare v) {
	/* de adaugat */
	/*
	* CF=TETA(1)-AMORTIZAT ; TREBUIE SA MODIFICAM UN ELEMENT CE SE AFLA PE PRIMA POZITIE
	* CD=TETA(N)-AMORTIZAT ; TREBUIE SA ADAUGAM UN ELEMENT NOU
	* CM=O(N)-AMORTIZAT
	* CG=O(N)
	*/
	TElem element(c,v);

	if (prim == -1)
	{
		int nou = creeazaNod(element);
		prim = nou;
		ultim = nou;
		return NULL_TVALOARE;
	}

	int pozitie = prim;
	while (elemente[pozitie].first != c && pozitie != ultim)
	{
		pozitie = urmator[pozitie];
	}
	if (pozitie == ultim && elemente[pozitie].first == c)
	{
		TValoare veche = elemente[pozitie].second;
		elemente[pozitie].second = v;
		return veche;
	}
	else if (pozitie == ultim)
	{
		int nou = creeazaNod(element);
		precedent[nou] = ultim;
		urmator[nou] = urmator[ultim];
		//std::cout << nou << std::endl;
		urmator[ultim] = nou;
		ultim = nou;
		return NULL_TVALOARE;
	}
	else
	{
		TValoare veche = elemente[pozitie].second;
		elemente[pozitie].second = v;
		return veche;
	}

}



//cauta o cheie si returneaza valoarea asociata (daca dictionarul contine cheia) sau null
TValoare Dictionar::cauta(TCheie c) const {
	/* de adaugat */
	/*
	* CF=TETA(1) ; TREBUIE SA CAUTAM UN ELEMENT CE SE AFLA PE PRIMA POZITIE
	* CD=TETA(N) ; TREBUIE SA CAUTAM UN ELEMENT CE SE AFLA PE ULTIMA POZITIE SAU NU EXISTA
	* CM=O(N)
	* CG=O(N)
	*/
	int pozitie = prim;
	while (elemente[pozitie].first != c && pozitie != ultim)
	{
		pozitie = urmator[pozitie];
	}
	if (pozitie == ultim && elemente[pozitie].first == c)
	{
		return elemente[pozitie].second;
	}
	else if (pozitie == ultim)
	{
		return NULL_TVALOARE;
	}
	else
	{
		return elemente[pozitie].second;
	}
}


TValoare Dictionar::sterge(TCheie c) {
	/* de adaugat */
	/*
	* CF=TETA(1) ; TREBUIE SA STERGEM UN ELEMENT CE SE AFLA PE PRIMA POZITIE
	* CD=TETA(N) ; TREBUIE SA STERGEM UN ELEMENT CE SE AFLA PE ULTIMA POZITIE SAU NU EXISTA
	* CM=O(N)
	* CG=O(N)
	*/
	int pozitie = prim;
	if (pozitie == -1)
	{
		return NULL_TVALOARE;
	}
	while (elemente[pozitie].first != c && pozitie != ultim)
	{
		pozitie = urmator[pozitie];
	}
	if (pozitie == ultim && elemente[pozitie].first == c)
	{
		if (pozitie == prim)
		{
			prim = -1;
			ultim = -1;
			dealoca(pozitie);
			return elemente[pozitie].second;
		}
		int prec = precedent[pozitie];
		urmator[prec] = -1;
		ultim = prec;
		dealoca(pozitie);
		return elemente[pozitie].second;
	}
	else if (pozitie == ultim)
	{
		return NULL_TVALOARE;
	}
	else
	{
		if (pozitie == prim)
		{
			int urm = urmator[pozitie];
			precedent[urm] = -1;
			//std::cout << urm << std::endl;
			prim = urm;
			dealoca(pozitie);
			return elemente[pozitie].second;
		}
		int prec = precedent[pozitie];
		int urm = urmator[pozitie];
		urmator[prec] = urm;
		precedent[urm] = prec;
		//std::cout << urm << std::endl;
		dealoca(pozitie);
		return elemente[pozitie].second;
	}
}


int Dictionar::dim() const {
	/* de adaugat */
	/*
	* CF=CD=CM=CG=TETA(N)
	*/
	int pozitie = prim;
	int dimensiune = 0;
	while (pozitie != -1)
	{
		dimensiune++;
		pozitie = urmator[pozitie];
	}
	return dimensiune;
}

bool Dictionar::vid() const {
	/* de adaugat */
	/*
	* CF=CD=CM=CG=TETA(1)
	*/
	if (prim == -1)
	{
		return true;
	}
	else
	{
		return false;

	}
}

int Dictionar::diferentaValoareMaxMin() const
{
	/*
	CD=CF=CM=CG=teta(n) - n numarul de elemente din dictionar
	*/
	int pozitie = prim;
	if (pozitie == -1)
		return -1;
	else
	{
		int valMax = elemente[pozitie].second;
		int valMin = elemente[pozitie].second;
		pozitie = urmator[pozitie];
		while (pozitie != -1)
		{
			if (valMax < elemente[pozitie].second)
				valMax = elemente[pozitie].second;
			if (valMin > elemente[pozitie].second)
				valMin = elemente[pozitie].second;
			pozitie = urmator[pozitie];
		}
		return valMax - valMin;
	}
}


IteratorDictionar Dictionar::iterator() const {
	/*
	* CF=CD=CM=CG=TETA(1)
	*/
	return  IteratorDictionar(*this);
}


