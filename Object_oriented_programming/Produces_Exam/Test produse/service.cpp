#include "service.h"

vector<Produs> SrvProduse::getAll()
{
	auto v = repo.getAll();
	sort(v.begin(), v.end(), [&](Produs p1, Produs p2) {return p1.getPret() < p2.getPret(); });
	return v;
}


void SrvProduse::adauga(int id, string nume, string tip, double pret)
{
	Produs p{ id, nume, tip, pret };
	p.valideaza();
	repo.adauga(p);
}

int SrvProduse::acelasiTip(string tip)
{
	auto v = repo.getAll();
	int nr = 0;
	for (auto each : v)
	{
		if (each.getTip() == tip)
		{
			nr++;
		}
	}
	return nr;
}