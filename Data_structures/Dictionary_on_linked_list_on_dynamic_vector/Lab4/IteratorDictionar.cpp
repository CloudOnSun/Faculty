#include "IteratorDictionar.h"
#include "Dictionar.h"
#include <exception>

using namespace std;

IteratorDictionar::IteratorDictionar(const Dictionar& d) : dict(d) {
	/* de adaugat */
	/*
	* CF=CD=CM=CG=TETA(1)
	*/
	pozitie = dict.prim;
}


void IteratorDictionar::prim() {
	/* de adaugat */
	/*
	* CF=CD=CM=CG=TETA(1)
	*/
	pozitie = dict.prim;
}


void IteratorDictionar::urmator() {
	/* de adaugat */
	/*
	* CF=CD=CM=CG=TETA(1)
	*/
	if (pozitie == -1)
		throw std::exception();
	pozitie = dict.urmator[pozitie];
}


TElem IteratorDictionar::element() const {
	/* de adaugat */
	/*
	* CF=CD=CM=CG=TETA(1)
	*/
	if (pozitie == -1)
		throw std::exception();
	return dict.elemente[pozitie];
}


bool IteratorDictionar::valid() const {
	/* de adaugat */
	/*
	* CF=CD=CM=CG=TETA(1)
	*/
	if (pozitie == -1)
	{
		return false;
	}
	return true;
}

