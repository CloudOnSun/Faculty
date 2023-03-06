#pragma once
#include "domain.h"

class RepoTractoare
{
private:

	string file;
	vector<Tractor> tractoare;
	void citesteDinFisier();
	void incarcaInFisier();

public:

	RepoTractoare(string f) : file{ f } { citesteDinFisier(); }

	vector<Tractor> getAll();

	void adauga(Tractor t);

	void modifica(int id, int roti);
};