/********************************************************************************
** Form generated from reading UI file 'TestTractoare.ui'
**
** Created by: Qt User Interface Compiler version 6.3.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_TESTTRACTOARE_H
#define UI_TESTTRACTOARE_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_TestTractoareClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *TestTractoareClass)
    {
        if (TestTractoareClass->objectName().isEmpty())
            TestTractoareClass->setObjectName(QString::fromUtf8("TestTractoareClass"));
        TestTractoareClass->resize(600, 400);
        menuBar = new QMenuBar(TestTractoareClass);
        menuBar->setObjectName(QString::fromUtf8("menuBar"));
        TestTractoareClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(TestTractoareClass);
        mainToolBar->setObjectName(QString::fromUtf8("mainToolBar"));
        TestTractoareClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(TestTractoareClass);
        centralWidget->setObjectName(QString::fromUtf8("centralWidget"));
        TestTractoareClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(TestTractoareClass);
        statusBar->setObjectName(QString::fromUtf8("statusBar"));
        TestTractoareClass->setStatusBar(statusBar);

        retranslateUi(TestTractoareClass);

        QMetaObject::connectSlotsByName(TestTractoareClass);
    } // setupUi

    void retranslateUi(QMainWindow *TestTractoareClass)
    {
        TestTractoareClass->setWindowTitle(QCoreApplication::translate("TestTractoareClass", "TestTractoare", nullptr));
    } // retranslateUi

};

namespace Ui {
    class TestTractoareClass: public Ui_TestTractoareClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_TESTTRACTOARE_H
