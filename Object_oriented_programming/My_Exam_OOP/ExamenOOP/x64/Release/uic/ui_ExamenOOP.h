/********************************************************************************
** Form generated from reading UI file 'ExamenOOP.ui'
**
** Created by: Qt User Interface Compiler version 6.3.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_EXAMENOOP_H
#define UI_EXAMENOOP_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_ExamenOOPClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *ExamenOOPClass)
    {
        if (ExamenOOPClass->objectName().isEmpty())
            ExamenOOPClass->setObjectName(QString::fromUtf8("ExamenOOPClass"));
        ExamenOOPClass->resize(600, 400);
        menuBar = new QMenuBar(ExamenOOPClass);
        menuBar->setObjectName(QString::fromUtf8("menuBar"));
        ExamenOOPClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(ExamenOOPClass);
        mainToolBar->setObjectName(QString::fromUtf8("mainToolBar"));
        ExamenOOPClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(ExamenOOPClass);
        centralWidget->setObjectName(QString::fromUtf8("centralWidget"));
        ExamenOOPClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(ExamenOOPClass);
        statusBar->setObjectName(QString::fromUtf8("statusBar"));
        ExamenOOPClass->setStatusBar(statusBar);

        retranslateUi(ExamenOOPClass);

        QMetaObject::connectSlotsByName(ExamenOOPClass);
    } // setupUi

    void retranslateUi(QMainWindow *ExamenOOPClass)
    {
        ExamenOOPClass->setWindowTitle(QCoreApplication::translate("ExamenOOPClass", "ExamenOOP", nullptr));
    } // retranslateUi

};

namespace Ui {
    class ExamenOOPClass: public Ui_ExamenOOPClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_EXAMENOOP_H
