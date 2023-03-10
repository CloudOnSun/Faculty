#pragma once

typedef int TElem;

typedef bool(*Relatie)(TElem, TElem);

//in implementarea operatiilor se va folosi functia (relatia) rel (de ex, pentru <=)
// va fi declarata in .h si implementata in .cpp ca functie externa colectiei
bool rel(TElem, TElem);

class IteratorMultime;

class Nod;

typedef Nod *PNod;

class Nod

{

public:

	friend class Multime;
	friend class IteratorMultime;

	//constructor

	Nod(PNod prec, TElem e, PNod urm);

	TElem element();

	PNod precedent();

	PNod urmator();

private:

	PNod prec;

	TElem e;

	PNod urm;

};

class Multime {

	friend class IteratorMultime;

private:
	/* aici e reprezentarea */

	PNod prim;
	PNod ultim;
	Relatie relatie;

public:
	//constructorul implicit
	Multime();

	//adauga un element in multime
	//returneaza adevarat daca elementul a fost adaugat (nu exista deja in multime)
	bool adauga(TElem e);

	//sterge un element din multime
	//returneaza adevarat daca elementul a existat si a fost sters
	bool sterge(TElem e);

	//verifica daca un element se afla in multime
	bool cauta(TElem elem) const;


	//intoarce numarul de elemente din multime;
	int dim() const;

	//verifica daca multimea e vida;
	bool vida() const;

	//returneaza un iterator pe multime
	IteratorMultime iterator() const;

	//verifica daca multimea curenta este o submultime a multimii primite ca parametru
	bool submultime(Multime& m) const;

	// destructorul multimii
	~Multime();

};

