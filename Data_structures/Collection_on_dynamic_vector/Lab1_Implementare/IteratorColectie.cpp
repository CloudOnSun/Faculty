#include "IteratorColectie.h"
#include "Colectie.h"


IteratorColectie::IteratorColectie(const Colectie& c) : col(c) {
	/* de adaugat */
	/*
	CF=teta(1)
	CD=teta(1)
	CM=teta(1)
	CG=teta(1)
	*/
	curent = 0;
}

void IteratorColectie::prim() {
	/* de adaugat */
	/*
	CF=teta(1)
	CD=teta(1)
	CM=teta(1)
	CG=teta(1)
	*/
	curent = 0;
}


void IteratorColectie::urmator() {
	/* de adaugat */
	/*
	CF=teta(1)
	CD=teta(1)
	CM=teta(1)
	CG=teta(1)
	*/
	curent++;
	if (!valid())
	{
		return;
	}
}


bool IteratorColectie::valid() const {
	/* de adaugat */
	/*
	CF=teta(1)
	CD=teta(1)
	CM=teta(1)
	CG=teta(1)
	*/
	if (curent == col.lungime_poziti)
	{
		return false;
	}
	return true;
}



TElem IteratorColectie::element() const {
	/* de adaugat */
	/*
	CF=teta(1)
	CD=teta(1)
	CM=teta(1)
	CG=teta(1)
	*/
	TElem elem = col.vector_elemente[col.vector_poziti[curent]];
	return elem;
}
