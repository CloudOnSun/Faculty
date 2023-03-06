#pragma once
#include <qwidget.h>
#include <qboxlayout.h>
#include <qtableview.h>
#include <QAbstractTableModel>
#include "service.h"
#include <qlineedit.h>
#include <qpushbutton.h>
#include <qcombobox.h>
#include <qcolor.h>
#include <qvariant.h>
#include <qpainter.h>
#include <qevent.h>


class TableModel : public QAbstractTableModel
{
	vector<Tractor> tractoare;
	string tip = "";

public:

	TableModel(vector<Tractor> v): tractoare{v} {}

	int rowCount(const QModelIndex& parent = QModelIndex()) const override
	{
		return tractoare.size();
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
			Tractor t = tractoare.at(row);
			if (col == 0)
			{
				return QString::number(t.getId());
			}
			if (col == 1)
			{
				return QString::fromStdString(t.getDenumire());
			}
			if (col == 2)
			{
				return QString::fromStdString(t.getTip());
			}
			if (col == 3)
			{
				return QString::number(t.getNr());
			}
			if (col == 4)
			{
				int nr = 0;
				for (auto t2 : tractoare)
				{
					if (t2.getTip() == t.getTip())
					{
						nr++;
					}
				}
				return QString::number(nr);

			}
		}
		if (role == Qt::BackgroundRole)
		{
			Tractor t = tractoare.at(row);
			if (t.getTip() == tip)
			{
				return QColor(Qt::red);
			}
		}
		return QVariant();
	}


	void setTrac(vector<Tractor> t, string tip = "")
	{
		tractoare = t;
		this->tip = tip;
		auto topLeft = createIndex(0, 0);
		auto botRight = createIndex(rowCount(), columnCount());
		emit dataChanged(topLeft, botRight);
		emit layoutChanged();
	}


	/*Qt::ItemFlags flags(const QModelIndex&) const
	{
		return Qt::ItemIsSelectable | Qt::ItemIsEditable | Qt::ItemIsEnabled;
	}*/


	QVariant headerData(int section, Qt::Orientation ori, int role) const
	{
		if (role == Qt::DisplayRole)
		{
			if (ori == Qt::Horizontal)
			{
				switch (section)
				{
				case 0:
					return tr("Id");
				case 1:
					return tr("Denumire");
				case 2:
					return tr("Tip");
				case 3:
					return tr("Roti");
				case 4:
					return tr("Acelasi tip");
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
private:

	RepoTractoare repo{ "fisierDate.txt" };
	SrvTrac srv{ repo };

	QTableView* tableView;
	TableModel* tableModel;

	QLineEdit* linieID = new QLineEdit;
	QLineEdit* linieDen = new QLineEdit;
	QLineEdit* linieTip = new QLineEdit;
	QLineEdit* linieRoti = new QLineEdit;
	QPushButton* butonAduaga = new QPushButton("Adauga");


	QComboBox* cBox = new QComboBox();


 public:

	MainActivity(QWidget* parent = nullptr);

	void connect();

	void updateCombo();

	void paintEvent(QPaintEvent* ev) override
	{
		QPainter p{ this };

		if (tableView->selectionModel()->selectedIndexes().isEmpty())
		{
			p.drawPoint(0, 0);
			return;
		}

		auto row = tableView->selectionModel()->selectedIndexes().at(0).row();
		auto celIndex = tableView->model()->index(row, 3);
		int roti = tableView->model()->data(celIndex, Qt::DisplayRole).toInt();
		for (int i = 1; i <= roti; i++)
		{
			p.drawEllipse(width() - i*50, height() - 50, 50, 50);
		}
	}

	void mousePressEvent(QMouseEvent* ev) override
	{
		if (tableView->selectionModel()->selectedIndexes().isEmpty())
		{
			return;
		}
		auto poz = ev->pos();
		auto row = tableView->selectionModel()->selectedIndexes().at(0).row();
		auto celIndex = tableView->model()->index(row, 3);
		int roti = tableView->model()->data(celIndex, Qt::DisplayRole).toInt();
		bool acolo = false;
		int x = poz.x();
		int y = poz.y();
		if (y < height() - 50)
			return;
		for (int i = 1; i <= roti; i++)
		{
			int cx = width() - i * 50;
			int cy = height() - 50;
			if ((x - cx) * (x - cx) + (y - cy) * (y - cy) <= 50 * 50)
			{
				acolo = true;
			}
		}
		if (!acolo)
		{
			return;
		}
		roti = roti - 2;
		celIndex = tableView->model()->index(row, 0);
		int id = tableView->model()->data(celIndex, Qt::DisplayRole).toInt();
		srv.modifica(id, roti);
		tableModel->setTrac(srv.getAll());
		repaint();
	}

};