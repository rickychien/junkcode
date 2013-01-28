#include "qtogre.h"

const QPoint QtOgre::invalidMousePoint(-1, -1);

QtOgre::QtOgre(QWidget *parent, Qt::WFlags flags)
	: QWidget(parent, flags)
	, mOgreRoot(NULL)
	, mOgreWindow(NULL)
	, mCamera(NULL)
	, mViewport(NULL)
	, mSceneMgr(NULL)
	, mMainLight(NULL)
	, mHeadNode(NULL)
	, mRenderSystem(NULL)
	, moveOffset(5)
	, mOldMousePos(invalidMousePoint)
{
	ui.setupUi(this);
	createOgreRoot();
	setupOgreRenderSystem();
	createOgreRenderWindow();
	initOgreScene();
	loadScene();
	startTimer(10);

	monitorThread = new boost::thread(&QtOgre::monitorThreadRun, ui);
}

QtOgre::~QtOgre()
{
	release();
}

void QtOgre::createOgreRoot()
{
	mOgreRoot = new Ogre::Root("plugins_d.cfg", "ogre_d.cfg", "ogre.log"); 
}

int QtOgre::setupOgreRenderSystem()
{
	Ogre::RenderSystemList::const_iterator renderers;
	renderers = mOgreRoot->getAvailableRenderers().begin();
 
	while(renderers != mOgreRoot->getAvailableRenderers().end()) {
		Ogre::String rName = (*renderers)->getName();
		if (rName == "OpenGL Rendering Subsystem"){
			mOgreRoot->setRenderSystem(*renderers);
			return true;
		}
		renderers++;
	}
	return false;
}

void QtOgre::createOgreRenderWindow()
{
	Ogre::String winHandle;
	Ogre::NameValuePairList params;
 
	// 1. initialize without creating window
	mOgreRoot->initialise(false); 
 
	//2. pass OgreWidget WId to parameter "externalWindowHandle" for creating renderwindow
	winHandle = Ogre::StringConverter::toString((size_t)(HWND) ui.widget->winId());
	params["externalWindowHandle"] = winHandle;
	
	//3. set OgreWidget size to create render window
	mOgreWindow = mOgreRoot->createRenderWindow("QtOgreWidget_RenderWindow", 
												width(), 
												height(),
												false,
												&params );
}

void QtOgre::initOgreScene()
{ 
	Ogre::SceneType scene_manager_type = Ogre::ST_GENERIC;
	mSceneMgr = mOgreRoot->createSceneManager( scene_manager_type );
 
	mSceneMgr->setAmbientLight(Ogre::ColourValue(0.5, 0.5, 0.5));
	mMainLight = mSceneMgr->createLight("MainLight");
	mMainLight->setPosition(20, 80, 50);
 
	mCamera = mSceneMgr->createCamera("OgreWidgetCam");
	mCamera->setPosition(Ogre::Vector3(0,0,200));
	mCamera->lookAt(Ogre::Vector3(0,0,-300));
	mCamera->setNearClipDistance(1);
  
	mViewport = mOgreWindow->addViewport( mCamera );
	mViewport->setBackgroundColour(Ogre::ColourValue::Black);
	mCamera->setAspectRatio(Ogre::Real(mViewport->getActualWidth()) / Ogre::Real(mViewport->getActualHeight()));

	loadResources();
}

void QtOgre::loadResources() 
{  
    // Load resource paths from config file  
#ifdef _DEBUG
    mResourcesCfg = "resources_d.cfg";
    mPluginsCfg = "plugins_d.cfg";
#else
    mResourcesCfg = "resources.cfg";
    mPluginsCfg = "plugins.cfg";
#endif
    Ogre::ConfigFile cf;  
    cf.load(mResourcesCfg);  
    // Go through all sections & settings in the file  
    Ogre::ConfigFile::SectionIterator seci = cf.getSectionIterator();  
    Ogre::String secName, typeName, archName;  
    while (seci.hasMoreElements()) {  
        secName = seci.peekNextKey();  
        Ogre::ConfigFile::SettingsMultiMap *settings = seci.getNext();  
        Ogre::ConfigFile::SettingsMultiMap::iterator i;  
        for (i = settings->begin(); i != settings->end(); ++i) {  
            typeName = i->first;  
            archName = i->second;  
#if OGRE_PLATFORM == OGRE_PLATFORM_APPLE  
            // OS X does not set the working directory relative to the app,  
            // In order to make things portable on OS X we need to provide  
            // the loading with it's own bundle path location  
            Ogre::ResourceGroupManager::getSingleton().addResourceLocation(  
                        Ogre::String(macBundlePath() + "/" + archName), typeName, secName);  
#else  
            Ogre::ResourceGroupManager::getSingleton().addResourceLocation(archName, typeName, secName);  
#endif  
        }  
    }  
    // Initialise, parse scripts etc  
    Ogre::ResourceGroupManager::getSingleton().initialiseAllResourceGroups();  
}

void QtOgre::loadScene()
{
	Ogre::Entity* ogreHead = mSceneMgr->createEntity("head", "ogrehead.mesh");
	mHeadNode = mSceneMgr->getRootSceneNode()->createChildSceneNode();
	mHeadNode->attachObject(ogreHead);
}

void QtOgre::release()
{
	mOgreWindow->removeAllViewports();
	mOgreRoot->detachRenderTarget(mOgreWindow);
	mSceneMgr->clearScene();
	mOgreRoot->destroySceneManager(mSceneMgr);
	mOgreRoot->shutdown();

	delete mOgreRoot;
}

void QtOgre::render()
{
	if(!mOgreRoot ||!mOgreWindow) {
		return;
	}

	mOgreRoot->_fireFrameStarted();
	mOgreWindow->update();
	mOgreRoot->_fireFrameRenderingQueued();
	mOgreRoot->_fireFrameEnded();
}

void QtOgre::resizeEvent(QResizeEvent* evt)
{
	if(!mOgreWindow || !mCamera){
		return;
	}
	mOgreWindow->windowMovedOrResized();
	mCamera->setAspectRatio(Ogre::Real(width()) / Ogre::Real(height()));
}

void QtOgre::timerEvent(QTimerEvent* evt)
{
	render();
}

void QtOgre::paintEvent(QPaintEvent* evt)
{
	render();
}

void QtOgre::keyPressEvent(QKeyEvent* evt)  
{  
	boost::format positionFormat("(%.1f,%.1f,%.1f)");
	positionFormat % mCamera->getPosition().x % mCamera->getPosition().y % mCamera->getPosition().z;
	sendData(positionFormat.str().c_str());
	
	Ogre::Vector3 movement;
    switch(evt->key()) {
		case Qt::Key_W:
			movement = Ogre::Vector3(0, 0, -moveOffset);
			break;
		case Qt::Key_S:
			movement = Ogre::Vector3(0, 0, moveOffset);
			break;
		case Qt::Key_A:
			movement = Ogre::Vector3(-moveOffset, 0, 0);
			break;
		case Qt::Key_D:
			movement = Ogre::Vector3(moveOffset, 0, 0);
			break;
	}

	mCamera->moveRelative(movement);
    evt->accept();
}

void QtOgre::mouseMoveEvent(QMouseEvent* evt)  
{
    if (evt->buttons().testFlag(Qt::LeftButton) && mOldMousePos != invalidMousePoint) {  
        Ogre::Real deltaX = evt->pos().x() - mOldMousePos.x();  
        Ogre::Real deltaY = evt->pos().y() - mOldMousePos.y();  

        Ogre::Vector3 camTranslation(deltaX, deltaY, 0);

		mCamera->yaw(Ogre::Degree(deltaX * -0.1));
		mCamera->pitch(Ogre::Degree(deltaY * -0.1));
        mOldMousePos = evt->pos();
        evt->accept();  
    } else {
        evt->ignore();  
	}
}  

void QtOgre::mousePressEvent(QMouseEvent* evt)  
{
	if (evt->buttons().testFlag(Qt::LeftButton)) {
		mOldMousePos = evt->pos();  
		evt->accept();  
	} else {
        evt->ignore(); 
	}
}  

void QtOgre::mouseReleaseEvent(QMouseEvent* evt)  
{  
    if (!evt->buttons().testFlag(Qt::LeftButton)) {  
        mOldMousePos = QPoint(invalidMousePoint);  
        evt->accept();  
    } else {
        evt->ignore();
	}
}  

void QtOgre::sendData(const char* cameraPosBuf)
{
	char mutableBuf[128];
	strcpy(mutableBuf, cameraPosBuf);
	try {
		using boost::asio::ip::tcp;
		boost::asio::io_service io_service;	
		tcp::resolver resolver(io_service);
		tcp::resolver::query query("127.0.0.1", "8086");
		tcp::resolver::iterator endpoint_iterator = resolver.resolve(query);
		tcp::socket socket(io_service);
		boost::asio::connect(socket, endpoint_iterator);
		boost::asio::write(socket, boost::asio::buffer(mutableBuf));
	}
	catch (std::exception& e)
	{
		std::cerr << e.what() << std::endl;
	}
}

void QtOgre::monitorThreadRun(Ui::QtOgreClass& ui)
{
	using boost::asio::ip::tcp;
	char buf[128];
	try {
		boost::asio::io_service io_service;
		tcp::acceptor acceptor(io_service, tcp::endpoint(tcp::v4(), 8086));
		for (;;) {
			tcp::socket socket(io_service);
			acceptor.accept(socket);
			boost::system::error_code error;
			size_t len = socket.read_some(boost::asio::buffer(buf), error);
			qDebug() << buf;

			QString positionText = QString::fromUtf8(buf);
			ui.positionLabel->setText(positionText);
		}
	}
	catch (std::exception& e)
	{
		std::cerr << e.what() << std::endl;
	}
}
