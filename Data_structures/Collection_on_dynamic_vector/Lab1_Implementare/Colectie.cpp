#include "Colectie.h"
#include "IteratorColectie.h"
#include <exception>
#include <iostream>

using namespace std;


void Colectie::redimensionare_elemente()
{
	/*
	CF=teta(n)
	CD=teta(n)
	CM=teta(n)
	CG=teta(n)
	*/
	TElem *vector_elemente_nou = new TElem[2 * capacitate_elemente];
	for (int i = 0; i < lungime_elemente; i++)
	{
		vector_elemente_nou[i] = vector_elemente[i];
	}
	capacitate_elemente = capacitate_elemente * 2;
	delete[] vector_elemente;
	vector_elemente = vector_elemente_nou;
}


void Colectie::redimensionare_poziti()
{
	/*
	CF=teta(n)
	CD=teta(n)
	CM=teta(n)
	CG=teta(n)
	*/
	TElem* vector_poziti_nou = new TElem[2 * capacitate_poziti];
	for (int i = 0; i < lungime_poziti; i++)
	{
		vector_poziti_nou[i] = vector_poziti[i];
	}
	capacitate_poziti = capacitate_poziti * 2;
	delete[] vector_poziti;
	vector_poziti = vector_poziti_nou;
}


Colectie::Colectie() {
	/* de adaugat */
	/*
	CF=teta(1)
	CD=teta(1)
	CM=teta(1)
	CG=teta(1)
	*/
	this->lungime_elemente = 0;
	this->capacitate_elemente = 2;
	vector_elemente = new TElem[capacitate_elemente];
	this->lungime_poziti = 0;
	this->capacitate_poziti = 2;
	vector_poziti = new TElem[capacitate_poziti];
}


void Colectie::adauga(TElem elem) {
	/* de adaugat */
	/*
	CF=teta(1) ;elementul dorit se afla deja pe prima pozitie
	CD=teta(n) ;elementul dorit nu exista sau se afla pe ultima pozitie
	CM=O(n)
	CG=O(n)
	*/

	bool gasit = false;
	for (int i = 0; i < lungime_elemente; i++)
	{
		if (this->vector_elemente[i] == elem)
		{
			gasit = true;
			if (lungime_poziti == capacitate_poziti)
			{
				redimensionare_poziti();
			}
			this->vector_poziti[lungime_poziti] = i;
			lungime_poziti++;
			break;
		}
	}

	if (!gasit)
	{
		if (lungime_elemente == capacitate_elemente)
		{
			redimensionare_elemente();
		}
		if (lungime_poziti == capacitate_poziti)
		{
			redimensionare_poziti();
		}
		this->vector_elemente[lungime_elemente] = elem;
		this->vector_poziti[lungime_poziti] = lungime_elemente;
		lungime_elemente++;
		lungime_poziti++;
	}
	
}


bool Colectie::sterge(TElem elem) {
	/* de adaugat */
	/*
	CF=teta(n) 
	CD=teta(n)
	CM=teta(n)
	CG=teta(n)
	*/
	for (int i = 0; i < lungime_elemente; i++)
	{
		if (vector_elemente[i] == elem)
		{
			int nrAp = 0;
			int pozitie = 0;
			for (int j = i; j < lungime_poziti; j++)
			{
				if (vector_poziti[j] == i)
				{
					nrAp++;
					pozitie = j;
				}
			}
			if (nrAp == 1)
			{
				for (int j = pozitie; j < lungime_poziti - 1; j++)
				{
					if (vector_poziti[j + 1] > i)
					{
						vector_poziti[j + 1]--;
					}
					this->vector_poziti[j] = this->vector_poziti[j + 1];
				}
				lungime_poziti--;
				for (int j = i; j < lungime_elemente - 1; j++)
				{
					this->vector_elemente[j] = this->vector_elemente[j + 1];
				}
				lungime_elemente--;
				return true;
			}
			else
			{
				for (int j = pozitie; j < lungime_poziti-1; j++)
				{
					this->vector_poziti[j] = this->vector_poziti[j + 1];
				}
				lungime_poziti--;
				return true;
			}
			break;
		}
	}
	return false;
}


bool Colectie::cauta(TElem elem) const {
	/* de adaugat */
	/*
	CF=teta(1)
	CD=teta(n)
	CM=O(n)
	CG=O(n)
	*/
	for (int i = 0; i < lungime_elemente; i++)
	{
		if (vector_elemente[i] == elem)
		{
			return true;
		}
	}
	return false;
}

int Colectie::nrAparitii(TElem elem) const {
	/* de adaugat */
	/*
	CF=teta(n)
	CD=teta(n)
	CM=teta(n)
	CG=teta(n)
	*/
	for (int i = 0; i < lungime_elemente; i++)
	{
		if (vector_elemente[i] == elem)
		{
			int nrAp = 0;
			for (int j = i; j < lungime_poziti; j++)
			{
				if (vector_poziti[j] == i)
				{
					nrAp++;
				}
			}
			return nrAp;
		}
	}

	return 0;
}


int Colectie::dim() const {
	/* de adaugat */
	/*
	CF=teta(1)
	CD=teta(1)
	CM=teta(1)
	CG=teta(1)
	*/
	return lungime_poziti;
}


bool Colectie::vida() const {
	/* de adaugat */
	/*
	CF=teta(1)
	CD=teta(1)
	CM=teta(1)
	CG=teta(1)
	*/
	if (lungime_elemente == 0)
	{
		return true;
	}
	return false;
}

IteratorColectie Colectie::iterator() const {
	/*
	CF=teta(1)
	CD=teta(1)
	CM=teta(1)
	CG=teta(1)
	*/
	return  IteratorColectie(*this);
}


Colectie::~Colectie() {
	/* de adaugat */
	/*
	CF=teta(1)
	CD=teta(1)
	CM=teta(1)
	CG=teta(1)
	*/
	delete[] vector_elemente;
	delete[] vector_poziti;
}


