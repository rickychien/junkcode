#ifndef UML_CREATE_MODE_H
#define UML_CREATE_MODE_H

#include "uml_mode.h"
#include "uml_object.h"
#include "uml_class.h"
#include "uml_usecase.h"
#include "uml_scene.h"
#include <QtGui/QGraphicsSceneMouseEvent>

class CreateMode : public UMLMode {
public:
	CreateMode();
	CreateMode( UMLScene * delegateScene, UMLObject::Type objectType );
	virtual void onMousePress( QGraphicsSceneMouseEvent * event );

private:
	UMLObject::Type _umlObjectType;

};

#endif
