#include "TestScurt.h"
#include <assert.h>
#include "Matrice.h"
#include <iostream>

using namespace std;

void testAll() { //apelam fiecare functie sa vedem daca exista
	Matrice m(4, 4);
	assert(m.nrLinii() == 4);
	assert(m.nrColoane() == 4);
	//adaug niste elemente
	m.modifica(1, 1, 5);
	assert(m.element(1, 1) == 5);
	assert(m.modifica(1, 1, 6) == 5);
	assert(m.element(1, 2) == NULL_TELEMENT);
	m.modifica(1, 2, 6);
	m.modifica(0, 2, 7);
	m.modifica(0, 0, 5);
	pereche p1 = make_pair(0, 0);
	assert(m.positionOf(5) == p1);
	pereche p2 = make_pair(-1, 1);
	assert(m.positionOf(20) == p2);

	auto it = m.iterator();
	it.prim();
	assert(it.element() == 5);
	it.urmator();
	assert(it.element() == 6);
	it.urmator();
	assert(it.element() == 6);
	it.urmator();
	assert(it.element() == 7);
	it.urmator();
	assert(!it.valid());
	m.modifica(0, 0, 0);
	assert(m.element(0, 0) == NULL_TELEMENT);
	m.modifica(1, 2, 0);
	assert(m.element(1, 2) == NULL_TELEMENT);
}
