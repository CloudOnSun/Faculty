#include "Matrice.h"

#include <exception>

using namespace std;


int hashCode(TElem e) {
	/*
	* CD=CF=CM=CG=O(1);
	*/
	return abs(e);
}


int Matrice::disp(TElem e) const
{
	/*
	* CD=CF=CM=CG=teta(1);
	*/
	return hashCode(e) % this->m;
}


Matrice::Matrice(int nrLinii, int nrColoane) {
	/* de adaugat */
	/*
	* CD=CF=CM=CG=teta(m);
	*/
	if (nrLinii <= 0 || nrColoane <= 0)
		return;
	m = LUNG_TD;
	this->nrC = nrColoane;
	this->nrL = nrLinii;
	int i = 0;
	for (i = 0; i < m; i++)
	{
		this->td[i] = nullptr;
	}
}


Matrice::~Matrice()
{
	/*
	* CD=CF=CM=CG=teta(m);
	*/
	for (int i = 0; i < m; i++)
	{
		while (td[i] != nullptr)
		{
			PNod p = td[i];
			td[i] = td[i]->urm;
			delete p;
		}
	}
}


int Matrice::nrLinii() const {
	/* de adaugat */
	/*
	* CD=CF=CM=CG=teta(1);
	*/
	return nrL;
}


int Matrice::nrColoane() const {
	/* de adaugat */
	/*
	* CD=CF=CM=CG=teta(1);
	*/
	return nrC;
}


TElem Matrice::element(int i, int j) const {
	/* de adaugat */
	/*
	* CF=TETA(1) - ELEMENTUL CAUTAT SE AFLA PE PRIMA POZITIE
	* CD=TETA(N) - ELEMENTUL CAUTAT SE AFLA PE ULTIMA POZITIE
	* CM=O(N)
	* CG=O(N)
	*/
	if (i < 0 || j < 0 || i >= nrL || j >= nrC)
		return -1;
	TElem e = NULL_TELEMENT;
	for (int poz = 0; poz < m; poz++)
	{
		PNod nod = td[poz];
		while (nod != nullptr)
		{
			if (nod->lin == i && nod->col == j)
			{
				e = nod->e;
				break;
			}
			nod = nod->urm;
		}

		if (e != NULL_TELEMENT)
			break;
	}
	return e;
	
}


pereche Matrice::positionOf(TElem element) const
{
	/*
	* CF = teta(1) 
	* CD = teta(n/m)
	* CM = CG = O(n/m)
	*/
	int poz = disp(element);
	PNod nod = td[poz];
	while (nod != nullptr)
	{
		if (nod->e == element)
			break;
		nod = nod->urm;
	}
	pair<int, int> pozitie;

	if (nod == nullptr)
	{
		pozitie.first = -1;
		pozitie.second = 1;
	}
	else
	{
		pozitie.first = nod->lin;
		pozitie.second = nod->col;
	}
	return pozitie;
}



TElem Matrice::modifica(int i, int j, TElem e) {
	/* de adaugat */
	/*
	* CF=TETA(1) - ELEMENTUL CAUTAT SE AFLA PE PRIMA POZITIE DIN TD[POZ]
	* CD=TETA(N) - ELEMENTUL CAUTAT SE AFLA PE ULTIMA POZITIE DIN TD[POZ]
	* CM=O(N)
	* CG=O(N)
	*/
	if (i < 0 || j < 0 || i >= nrL || j >= nrC)
		return -1;
	TElem e2 = NULL_TELEMENT;
	if (e == 0)
	{
		for (int poz = 0; poz < m; poz++)
		{
			PNod nodP = td[poz];
			if (nodP != nullptr)
			{
				if (nodP->lin == i && nodP->col == j)
				{
					e2 = nodP->e;
					td[poz] = nodP->urm;
					delete nodP;
					return e2;
				}
				PNod nod = nodP->urm;
				while (nod != nullptr)
				{
					if (nod->lin == i && nod->col == j)
					{
						e2 = nod->e;
						nodP->urm = nod->urm;
						delete nod;
						return e2;
					}
					nodP = nod;
					nod = nod->urm;
				}
			}
		}
	}
	for (int poz = 0; poz < m; poz++)
	{
		PNod nod = td[poz];
		while (nod != nullptr)
		{
			if (nod->lin == i && nod->col == j)
			{
				e2 = nod->e;
				nod->e = e;
				return e2;
			}
			nod = nod->urm;
		}
	}
	int poz = disp(e);
	PNod nod = td[poz];
	PNod n = new Nod(e, td[poz], i, j);
	td[poz] = n;
	return NULL_TELEMENT;
}


Iterator Matrice::iterator()
{
	return Iterator(*this);
}


Iterator::Iterator(const Matrice& mat) : mat{ mat }
{
	poz = 0;
	deplasare();
}



void Iterator::deplasare()
{
	while (poz < mat.m && mat.td[poz] == nullptr)
	{
		poz++;
	}
	if (poz < mat.m)
	{
		curent = mat.td[poz];
	}
}


void Iterator::prim()
{
	poz = 0;
	deplasare();
}


void Iterator::urmator()
{
	curent = curent->urm;
	if (curent == nullptr)
	{
		poz = poz + 1;
		deplasare();
	}
}


bool Iterator::valid() const
{
	return poz < mat.m && curent != nullptr;
}


TElem Iterator::element() const
{
	return curent->e;
}