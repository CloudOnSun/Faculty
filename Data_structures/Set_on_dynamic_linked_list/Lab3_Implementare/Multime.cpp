#include "Multime.h"
#include "IteratorMultime.h"
#include <iostream>

using namespace std;


Nod::Nod(PNod prec, TElem e, PNod urm) {
	this->prec = prec;
	this->e = e;
	this->urm = urm;
}

PNod Nod::precedent()
{
	return prec;
}

TElem Nod::element() {
	return e;
}

PNod Nod::urmator() {
	return urm;
}



//o posibila relatie
bool rel(TElem e1, TElem e2) {
	if (e1 <= e2) {
		return true;
	}
	else {
		return false;
	}
}

Multime::Multime() {
	/* de adaugat */
	prim = nullptr;
	ultim = nullptr;
	relatie = rel;
}


bool Multime::adauga(TElem elem) {
	/* de adaugat */
	/*
	* CF = teta(1) amortizat -- trebuie sa adaugam chiar la inceput
	* CD = teta(n) amortizat -- trebuie sa aduagam la final
	* CM = O(n) amortizat
	* CG = O(n) amortizat
	*/
	PNod nod = new Nod(nullptr, elem, nullptr);
	PNod nodCauta = this->prim;
	if (nodCauta == nullptr)
	{
		prim = nod;
		ultim = nod;
		return true;
	}
	else if (rel(nod->e, nodCauta->e))
	{
		if (nodCauta->e == nod->e)
			return false;
		nod->urm = nodCauta;
		nodCauta->prec = nod;
		prim = nod;
		return true;
	}

	while (nodCauta->urm != nullptr && !rel(nod->e, nodCauta->e))
	{
		if (nodCauta->e == nod->e)
			return false;
		nodCauta = nodCauta->urm;
	}
	if (nodCauta->urm == nullptr && !rel(nod->e, nodCauta->e))
	{
		if (nodCauta->e == nod->e)
			return false;
		nodCauta->urm = nod;
		nod->prec = nodCauta;
		ultim = nod;
		return true;
	}
	if (nodCauta->e == nod->e)
		return false;
	nod->urm = nodCauta;
	nod->prec = nodCauta->prec;
	nodCauta->prec = nod;
	nod->prec->urm = nod;
	return true;
}


bool Multime::sterge(TElem elem) {
	/*
	* CF = teta(1) -- trebuie sa stergem primul element
	* CD = teta(n) -- trebuie sa stergem ultimul element sau un elemente ce nu exista si este mai mare decat toate cele din multime
	* CM = O(n) 
	* CG = O(n)
	*/
	PNod nodCauta = this->prim;
	if (nodCauta == nullptr)
	{
		return false;
	}
	if (nodCauta->prec == nullptr && nodCauta->e == elem)
	{
		if (nodCauta->urm != nullptr)
		{
			nodCauta->urm->prec = nodCauta->prec;
		}
		prim = nodCauta->urm;
		if (prim == nullptr)
		{
			ultim = nullptr;
		}
		delete nodCauta;
		return true;
	}
	while (nodCauta->urm != nullptr)
	{
		if (nodCauta->e == elem)
		{
			nodCauta->prec->urm = nodCauta->urm;
			nodCauta->urm->prec = nodCauta->prec;
			delete nodCauta;
			return true;
		}
		if (!rel(nodCauta->e, elem))
		{
			return false;
		}
		nodCauta = nodCauta->urm;
	}
	if (nodCauta->urm == nullptr && nodCauta->e == elem)
	{
		nodCauta->prec->urm = nodCauta->urm;
		ultim = nodCauta->prec;
		delete nodCauta;
		return true;
	}
	return false;
}


bool Multime::cauta(TElem elem) const {
	/* de adaugat */
	/*
	* CF = teta(1) -- trebuie sa cautam primul element
	* CD = teta(n) -- trebuie sa cautam ultimul element sau nu exista elementul
	* CM = O(n)
	* CG = O(n)
	*/

	PNod nodCauta = this->prim;
	while (nodCauta != nullptr)
	{
		if (nodCauta->e == elem)
		{
			return true;
		}
		nodCauta = nodCauta->urm;
	}
	return false;
}


int Multime::dim() const {
	/* de adaugat */
	/*
	* CF = CD = CM = CG = teta(n) -- trebuie sa parcurgem toate elementele
	*/
	int dimensiune = 0;
	PNod nodCauta = this->prim;
	while (nodCauta != nullptr)
	{
		dimensiune++;
		nodCauta = nodCauta->urm;
	}
	return dimensiune;
}



bool Multime::vida() const {
	/*
	* CF = CD = CM = CG = teta(1) 
	*/
	if (prim == nullptr)
		return true;
	else
		return false;
}

IteratorMultime Multime::iterator() const {
	return IteratorMultime(*this);
}

bool Multime::submultime(Multime& m) const
{
	/*
	* CF = teta(m) -- multimea are pe prima pozitie un element ce nu se afla in m
	* CD = teta(n) * O(m) -- n nr elem din mult noastra; m nr elem din multimea m -- mult este submult 
	* CM = O(n*m)
	* CG = O(n*m)
	*/
	auto it = iterator();
	it.prim();
	while (it.valid())
	{
		auto element = it.element();
		if (!m.cauta(element))
		{
			return false;
		}
		it.urmator();
	}
	return true;
}


Multime::~Multime() {
	/* de adaugat */
	/*
	* CF = CD = CM = CG = teta(n) -- trebuie sa parcurgem toate elementele
	*/
	while (prim != nullptr) {
		PNod p = prim;
		prim = prim->urm;
		delete p;
	}
}






