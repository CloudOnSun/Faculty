#include "IteratorMultime.h"
#include "Multime.h"


IteratorMultime::IteratorMultime(const Multime& m) : mult(m) {
	/* de adaugat */
	curent = mult.prim;
}

TElem IteratorMultime::element() const {
	/* de adaugat */
	/*
	* CF = CD = CM = CG = teta(1)
	*/
	if (valid())
	{
		return curent->e;
	}
	return -1;
}

bool IteratorMultime::valid() const {
	/* de adaugat */
	/*
	* CF = CD = CM = CG = teta(1)
	*/
	return curent != nullptr;
}

void IteratorMultime::urmator() {
	/* de adaugat */
	/*
	* CF = CD = CM = CG = teta(1)
	*/
	curent = curent->urm;
	if (!valid())
	{
		return;
	}
}

void IteratorMultime::prim() {
	/* de adaugat */
	/*
	* CF = CD = CM = CG = teta(1)
	*/
	curent = mult.prim;
}

