#pragma once
#include <string>

using namespace std;

class Melodie
{
private:
	int id;
	string titlu;
	string artist;
	int rank;

public:
	Melodie(int id, string titlu, string artist, int rank) : id{ id }, titlu{ titlu }, artist{ artist }, rank{ rank } { }
	int getId()
	{
		return id;
	}
	string getTitlu()
	{
		return titlu;
	}
	string getArtist()
	{
		return artist;
	}
	int getRank()
	{
		return rank;
	}
	void setTitlu(string titlu2)
	{
		titlu = titlu2;
	}
	void setRank(int rank2)
	{
		rank = rank2;
	}

	string toString()
	{
		return to_string(id) + "," + titlu + "," + artist + "," + to_string(rank);
	}

};