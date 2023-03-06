#pragma once
#include "repository.h"

class SrvTrac
{
private:

	RepoTractoare& repo;

public:

	SrvTrac(RepoTractoare& repo) : repo{ repo } {  }

	vector<Tractor> getAll();

	void adauga(int id, string den, string tip, int roti);

	vector<string> tipuri();

	void modifica(int id, int roti);
	
};