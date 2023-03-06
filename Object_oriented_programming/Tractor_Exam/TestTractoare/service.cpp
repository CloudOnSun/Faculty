#include "service.h"
#include <algorithm>

vector<Tractor> SrvTrac::getAll()
{
	auto v = repo.getAll();
	sort(v.begin(), v.end(), [&](Tractor t1, Tractor t2) {return t1.getDenumire() < t2.getDenumire(); });
	return v;
}

void SrvTrac::adauga(int id, string den, string tip, int roti)
{
	Tractor t(id, den, tip, roti);
	t.valideaza();
	repo.adauga(t);
}

vector<string> SrvTrac::tipuri()
{
	auto v = repo.getAll();
	vector<string> tip;
	for (auto each : v)
	{
		if (find(tip.begin(), tip.end(), each.getTip()) == tip.end())
		{
			tip.push_back(each.getTip());
		}
	}
	return tip;
}

void SrvTrac::modifica(int id, int roti)
{
	repo.modifica(id, roti);
}