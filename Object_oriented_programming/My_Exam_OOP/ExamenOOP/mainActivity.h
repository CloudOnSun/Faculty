#pragma once
#include "service.h"
#include <qwidget.h>
#include <qtableview.h>
#include <QAbstractTableModel>
#include <qlineedit.h>
#include <qpushbutton.h>

class TableModel : public QAbstractTableModel
{
	vector<Melodie> melodii;

public:


	TableModel(vector<Melodie> m) : melodii{ m } {  }

	/*
	* o functie care returneaza numarul de lini necesare = numarul de melodii;
	*/
	int rowCount(const QModelIndex& parent = QModelIndex()) const override
	{
		return melodii.size();
	}

	/*
	* o functie care returneaza numarul de coloane necesare = 6
	*/
	int columnCount(const QModelIndex& parent = QModelIndex()) const override
	{
		return 6;
	}

	/*
	* o functie care returneaza data necesarea fiecarei casute din tabel
	*/
	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override
	{
		int row = index.row();
		int col = index.column();
		if (role == Qt::DisplayRole)
		{
			Melodie m = melodii.at(row);
			if (col == 0)
			{
				return QString::number(m.getId());
			}
			if (col == 1)
			{
				return QString::fromStdString(m.getTitlu());
			}
			if (col == 2)
			{
				return QString::fromStdString(m.getArtist());
			}
			if (col == 3)
			{
				return QString::fromStdString(m.getGen());
			}
			if (col == 4)
			{
				int nr = 0;
				string artist = m.getArtist();
				for (auto each : melodii)
				{
					if (each.getArtist() == artist)
					{
						nr++;
					}
				}
				return QString::number(nr);
			}
			if (col == 5)
			{
				int nr = 0;
				string gen = m.getGen();
				for (auto each : melodii)
				{
					if (each.getGen() == gen)
					{
						nr++;
					}
				}
				return QString::number(nr);
			}
		}
		return QVariant();
	}

	/*
	* o functie care reseteaza vectorul de melodii si trimite semnale ca tabelul s-a schimbat
	*/
	void setMelodii(vector<Melodie> m)
	{
		melodii = m;
		auto topLeft = createIndex(0, 0);
		auto botRight = createIndex(rowCount(), columnCount());
		emit dataChanged(topLeft, botRight);
		emit layoutChanged();
	}

	/*
	* O functie pentru datele din headere
	*/
	QVariant headerData(int section, Qt::Orientation ori, int role) const override
	{
		if (role == Qt::DisplayRole)
		{
			if (ori == Qt::Horizontal)
			{
				switch (section)
				{
				case 0 :
					return tr("Id");
				case 1:
					return tr("Titlu");
				case 2:
					return tr("Artist");
				case 3:
					return tr("Gen");
				case 4:
					return tr("Acelasi artist");
				case 5:
					return tr("Acelasi gen");
				default:
					return QString("");
				}
			}
		}
		return QVariant();
	}
};


class MainActivity : public QWidget
{
	Q_OBJECT

private:

	RepoMelodii repo{ "fisierDate.txt" };
	SrvMelodii srv{ repo };

	QTableView* tableView = new QTableView;
	TableModel* tableModel = new TableModel(srv.getAll());

	QLineEdit* linieTitlu = new QLineEdit;
	QLineEdit* linieArtist = new QLineEdit;
	QLineEdit* linieGen = new QLineEdit;
	QPushButton* butonAdauga = new QPushButton("Adauga");
	QPushButton* butonSterge = new QPushButton("Sterge");


	/*
	* O functie care conecteaza butoanele cu functiile lambda corespunzatoare
	*/
	void connect();

public:

	MainActivity(QWidget* parent = nullptr);


	/*
	* O functie care deseneaza cercuri corespunzatoare genurilor in colturile ferestrei
	*/
	void paintEvent(QPaintEvent* ev) override;

};