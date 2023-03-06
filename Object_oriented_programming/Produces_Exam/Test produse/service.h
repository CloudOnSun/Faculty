#pragma once
#include "repository.h"
#include <algorithm>

class SrvProduse
{
private:
	RepoProduse& repo;

public:

	SrvProduse(RepoProduse& r) : repo{r} {}

	vector<Produs> getAll();

	void adauga(int id, string nume, string tip, double pret);

	int acelasiTip(string tip);
	
};