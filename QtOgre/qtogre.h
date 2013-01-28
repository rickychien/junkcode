#ifndef QTOGRE_H
#define QTOGRE_H

#include <QtGui>
#include <Ogre.h>
#include <boost/array.hpp>
#include <boost/asio.hpp>
#include <boost/format.hpp>
#include "ui_qtogre.h"

class QtOgre : public QWidget
{
	Q_OBJECT

public:
	QtOgre(QWidget *parent = 0, Qt::WFlags flags = 0);
	~QtOgre();

	virtual void resizeEvent(QResizeEvent* evt);
	virtual void timerEvent(QTimerEvent* evt);
	virtual void paintEvent(QPaintEvent* evt);
	virtual void keyPressEvent(QKeyEvent* evt);
	virtual void mouseMoveEvent(QMouseEvent* evt);
    virtual void mousePressEvent(QMouseEvent* evt);
    virtual void mouseReleaseEvent(QMouseEvent* evt);

protected:
	Ogre::Root *mOgreRoot;
	Ogre::RenderWindow *mOgreWindow;
	Ogre::Camera *mCamera;
	Ogre::Viewport *mViewport;
	Ogre::SceneManager *mSceneMgr;
	Ogre::Light *mMainLight;
	Ogre::SceneNode *mHeadNode;
	Ogre::RenderSystem *mRenderSystem;
	Ogre::Real moveOffset;

private:
	static void monitorThreadRun(Ui::QtOgreClass&);

	void createOgreRoot();
	int setupOgreRenderSystem();
	void createOgreRenderWindow();
	void initOgreScene();
	void loadResources();
	void loadScene();
	void render();
	void release();
	void sendData(const char*);

	static const QPoint invalidMousePoint;  

	Ui::QtOgreClass ui;
	Ogre::String mResourcesCfg;
	Ogre::String mPluginsCfg;
	QPoint mOldMousePos;
	boost::thread *monitorThread;

};

#endif // QTOGRE_H
