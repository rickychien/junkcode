#ifndef UML_LINKMODE_H
#define UML_LINKMODE_H

#include "uml_mode.h"
#include "uml_object.h"
#include "uml_link.h"
#include "uml_assoc_line.h"
#include "uml_composit_line.h"
#include "uml_general_line.h"
#include "uml_scene.h"
#include <QtGui/QGraphicsSceneMouseEvent>

class LinkMode : public UMLMode {
public:
	LinkMode();
	LinkMode( UMLScene * delegateScene, UMLLink::Type linkType );
	~LinkMode();

	virtual void onMousePress( QGraphicsSceneMouseEvent * event );
	virtual void onMouseMove( QGraphicsSceneMouseEvent * event );
	virtual void onMouseRelease( QGraphicsSceneMouseEvent * event );

private:
	UMLLink::Type _umlLinkType;
	QGraphicsLineItem * _mouseMoveLine;
	UMLObjectPort * _startPort;
	UMLObject * _startUMLObject;

};

#endif

