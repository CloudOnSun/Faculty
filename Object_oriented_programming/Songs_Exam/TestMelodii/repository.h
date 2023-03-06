#pragma once
#include <vector>
#include "domain.h"

class RepoMelodii
{
private:
	string file;
	vector <Melodie> melodii;
	void citesteDinFisier();
	void incarcaInFisier();

public:
	RepoMelodii(string file) : file{ file } { citesteDinFisier(); }

	vector<Melodie> getAll();

	void update(int id, string titlu, int rank);

	void sterge(int id);


};