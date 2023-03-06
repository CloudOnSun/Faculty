#pragma once


#define NULL_TVALOARE -1
typedef int TCheie;
typedef int TValoare;

class IteratorDictionar;

#include <utility>
typedef std::pair<TCheie, TValoare> TElem;

class Dictionar {
	friend class IteratorDictionar;

private:
	int prim, ultim, primLiber;
	int* urmator;
	int* precedent;
	TElem* elemente;
	int capacitate;

	// aloca un nod din inlantuirea nodurilor libere 
	// returneaza -1 daca este nevoie de redimensionare
	int aloca();

	// dealoca nodul de pe "pozitie", va trece in inlantuirea nodurilor libere
	void dealoca(int pozitie);

	//mareste capacitatea vectorilor alocati dinamic
	void redimensionare();

	//initializeaza inlantuirea nodurilor libere
	void initSpatiuLiber();

	//creeaza un nod pentru valoarea "element" fara sa initializeze urmator si precedent
	int creeazaNod(TElem element);

public:

	// constructorul implicit al dictionarului
	Dictionar();

	// adauga o pereche (cheie, valoare) in dictionar	
	//daca exista deja cheia in dictionar, inlocuieste valoarea asociata cheii si returneaza vechea valoare
	// daca nu exista cheia, adauga perechea si returneaza null: NULL_TVALOARE
	TValoare adauga(TCheie c, TValoare v);

	//cauta o cheie si returneaza valoarea asociata (daca dictionarul contine cheia) sau null: NULL_TVALOARE
	TValoare cauta(TCheie c) const;

	//sterge o cheie si returneaza valoarea asociata (daca exista) sau null: NULL_TVALOARE
	TValoare sterge(TCheie c);

	//returneaza numarul de perechi (cheie, valoare) din dictionar 
	int dim() const;

	//verifica daca dictionarul e vid 
	bool vid() const;

	int diferentaValoareMaxMin() const;

	// se returneaza iterator pe dictionar
	IteratorDictionar iterator() const;


	// destructorul dictionarului	
	~Dictionar();

};


