#pragma once
#include "service.h"
#include <qwidget.h>
#include <qtableview.h>
#include <QAbstractTableModel>
#include <qlineedit.h>
#include <qpushbutton.h>
#include <qlabel.h>
#include <qboxlayout.h>

class TableModel : public QAbstractTableModel
{
	vector<Produs> produse;
	int val = 0;

public:

	TableModel(vector<Produs> p) : produse{p} {}

	int rowCount(const QModelIndex& parent = QModelIndex()) const override
	{
		return produse.size();
	}

	int columnCount(const QModelIndex& parent = QModelIndex()) const override
	{
		return 5;
	}

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override
	{
		int row = index.row();
		int col = index.column();
		if (role == Qt::DisplayRole)
		{
			Produs p = produse.at(row);
			if (col == 0)
			{
				return QString::number(p.getId());
			}
			if (col == 1)
			{
				return QString::fromStdString(p.getNume());
			}
			if (col == 2)
			{
				return QString::fromStdString(p.getTip());
			}
			if (col == 3)
			{
				return QString::number(p.getPret());
			}
			if (col == 4)
			{
				int nr = 0;
				auto nume = p.getNume();
				for (int i = 0; i < nume.length(); i++)
				{
					if (nume[i] == 'a' || nume[i] == 'e' || nume[i] == 'i' || nume[i] == 'o' || nume[i] == 'u')
					{
						nr++;
					}
				}
				return QString::number(nr);
			}
		}
		if (role == Qt::BackgroundRole)
		{
			Produs p = produse.at(row);
			if (p.getPret() <= val)
			{
				return QColor(Qt::red);
			}
		}
		return QVariant();
	}

	void setProduse(vector<Produs> p, int v = 0)
	{
		val = v;
		produse = p;
		auto topLeft = createIndex(0, 0);
		auto botRight = createIndex(rowCount(), columnCount());
		emit dataChanged(topLeft, botRight);
		emit layoutChanged();
	}

	Qt::ItemFlags flags(const QModelIndex&) const
	{
		return Qt::ItemIsSelectable | Qt::ItemIsEditable | Qt::ItemIsEnabled;
	}

	QVariant headerData(int section, Qt::Orientation ori, int role) const override
	{
		if (role == Qt::DisplayRole)
		{
			if (ori == Qt::Horizontal)
			{
				switch (section)
				{
				case 0:
					return tr("ID");
				case 1:
					return tr("Nume");
				case 2:
					return tr("Tip");
				case 3:
					return tr("Pret");
				case 4:
					return tr("Nr vocale");
				default:
					return QString("");
				}
			}
		}
		return QVariant();
	}

};



class Observer : public QWidget
{
public:

	virtual void update() = 0;
	Observer() = default;
	virtual ~Observer() = default;

};


inline void notifyObserver(Observer* obs)
{
	obs->update();
}


class TipObs : public Observer
{

private:

	SrvProduse& srv;

	string tip;

	QLabel* l = new QLabel;

public:

	void update() override
	{
		int nr = srv.acelasiTip(tip);
		l->setText(QString::number(nr));
	}

	TipObs(string t, SrvProduse& s) : tip{ t }, srv{ s } {

		this->setWindowTitle(QString::fromStdString(tip));
		QHBoxLayout* hLay = new QHBoxLayout;
		hLay->addWidget(l);
		this->setLayout(hLay);
		update();
	}


};


class MainActivity : public QWidget
{

	Q_OBJECT

private:

	RepoProduse repo{ "fisierDate.txt" };
	SrvProduse srv{ repo };

	QTableView* tableView;
	TableModel* tableModel;

	QLineEdit* linieID = new QLineEdit;
	QLineEdit* linieNume = new QLineEdit;
	QLineEdit* linieTip = new QLineEdit;
	QLineEdit* liniePret = new QLineEdit;
	QPushButton* butonaduaga = new QPushButton("Adauga");

	QSlider* slider = new QSlider;

	vector<Observer*> obs;

	vector<string> tipuri;


	void connect();

public:

	MainActivity(QWidget* parent = nullptr);
};