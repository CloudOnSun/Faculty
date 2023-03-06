#pragma once
#include "domain.h"

class RepoProduse
{
private:
	string file;
	vector<Produs> produse;
	void citesteDinFisier();
	void incarcaInFisier();

public:

	RepoProduse(string f) : file{ f } { citesteDinFisier(); }

	vector<Produs> getAll();

	void adauga(Produs p);
};