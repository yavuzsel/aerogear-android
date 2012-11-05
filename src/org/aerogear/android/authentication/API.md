AeroGear Android Authentication API - DRAFT 0.1
============================

API Docs are available [here](http://aerogear.org/docs/specs/aerogear-android/)...

There is a [FAQ](http://aerogear.org/docs/guides/FAQ/) that talks about the API in a general fashion.

Below is a simple 'Getting started' section on how-to use the API

## Creating a pipeline and a pipe object

To create a pipeline, you need to use the Pipeline class. Below is an example: 



    
    String ROOT_URL = "http://todo-aerogear.rhcloud.com/todo-server";

    // create the 'todo' pipeline, which points to the baseURL of the REST application
    Pipeline pipeline = new Pipeline(ROOT_URL);

    // Add a REST pipe for the 'projects' endpoint
    Pipe<Project> projects = pipeline.pipe().name("projects")
                                .useClass(Project.class)
                                .buildAndAdd();


The Pipeline class offers some simple 'management' APIs to work with containing Pipe objects, which itself represents a server connection. The Pipe API is basically an abstraction layer for _any_ server side connection. In the example above the 'projects' pipe points to an RESTful endpoint (_http://todo-aerogear.rhcloud.com/todo-server/projects_). However, technical details like RESTful APIs (e.g. HTTP PUT) are not exposed on the Pipeline and Pipe APIs. Below is shown how to get access to an actual pipe, from the Pipeline object:

    // get access to the 'projects' pipe
    Pipe<Project> projects = pipeline.get("projects");

## Save data 

The Pipe offers an API to store newly created objects on a _remote_ server resource. These object are serialized by Google's GSON library. The 'save' method is described below:


    Project project = new Project();
    project.setName("Demo Project");
    project setDescription("This is a test project");
    // etc...

    // save the 'new' project:
    projects.save(projectEntity, new Callback<Project>(){
        void onSuccess(Project project) {
            alert("Success!");
        }
        void onFailure(Exception e) {
            alert("Failure!");
        }
    });

Above the _save_ function serializes the project object to JSON and then saves it on the server using a REST put or post.  

## Update data

The 'save' method (like in aerogear.js) is also responsible for updating an 'object'. However this happens _only_ when there is an 'getId()' method available:

## Remove data

The Pipe also contains a 'remove' method to delete the data on the server. It takes the value of the 'getId()' method, so that it knows which resource to delete:

    // get the 'id' value:
    String deleteId = "12345";

    // Now, just remove this project:
    projects.remove(deleteId, new Callback<Project>(){
        void onSuccess(Project project) {
            alert("Success!");
        }
        void onFailure(Exception e) {
            alert("Failure!");
        }
    });


In this case, where we have a RESTful pipe the API issues a HTTP DELETE request.

## Read all data from the server

The 'read' method allows to (currently) read _all_ data from the server, of the underlying Pipe:

    projects.read(Callback<List<Project>> {
        onSuccess(List<Project> projects) {
            for (Project p : projects) {
                System.out.println(project);
            }
        }
        
        onFailure(Exception e) {
            System.out.println("Failure!");
        }

    });

Since we are pointing to a RESTful endpoint, the API issues a HTTP GET request. The raw response of the above call looks like this:

	/*
            TODO put that in.
        */


DataManager
=============

## Create a datamanager with store object:

After receiving data from the server, your application may want to keep the data around. The DataManager API allows you to create Store instances. To create a datamanager, you need to use the DataManager class. Below is an example: 

	// create the datamanager
    DataManager* dm = [DataManager manager];
    // add a new (default) store object:
    id<Store> myStore = [dm add:^(id<StoreConfig> config) {
	        [config name:@"tasks"];
	    }];

The DataManager class offers some simple 'management' APIs to work with containing Store objects. The API offers read and write functionality. The default implementation represents an "in-memory" store. Similar to the pipe API technical details of the underlying system are not exposed.

## Save data to the Store

When using a pipe to read all entries of a endpoint, you can use the Store to save the received objects:

    ....
    id<Pipe> tasksPipe = [todo get:@"tasks"];
    ...

    [tasksPipe read:^(id responseObject) {
	    // the response object represents an NSArray,
	    // containing multile 'Tasks' (as NSDictionary objects)
	    [myStore save:responseObject success:^(id object) {

            // Indicate that the save operation was successful

        } failure:	^(NSError *error) {
            // when an error occurs... at least log it to the console..
            NSLog(@"Read: An error occured! \n%@", error);
		}];    

    } failure:^(NSError *error) {
        // when an error occurs... at least log it to the console..
        NSLog(@"Read: An error occured! \n%@", error);
    }];

When loading all tasks from the server, the Store object is used inside of the _read_ block from the Pipe object. The returned collection of tasks is stored inside our in-memory store, from where the data can be accessed.

## Read an object from the Store

    id taskObject;
    // read the task with the '0' ID:
    [myStore read:@"0" success:^(id object) {
        taskObject = object;
    } failure:^(NSError *error) {
        // when an error occurs... at least log it to the console..
        NSLog(@"Read: An error occured! \n%@", error);
    }];

The read accepts the _recordID_ and two simple blocks that are invoked on success or in case of an failure. The _readAll_ allows you to read the entire store, it accepts two simple blocks that are invoked on success or in case of an failure:

    // read all object from the store
    [myStore readAll:^(NSArray *objects) {

	    // work with the received collection, containing all objects

    } failure:^(NSError *error) {
        // when an error occurs... at least log it to the console..
        NSLog(@"Read: An error occured! \n%@", error);
    }];

## Remove one object

The remove function allows you to delete a single entry in the collection, if present:

    // remove the task with the '0' ID:
    [myStore remove:@"0" success:^(id object) {
        taskObject = object;
    } failure:^(NSError *error) {
        // when an error occurs... at least log it to the console..
        NSLog(@"Read: An error occured! \n%@", error);
    }];

The remove method accepts the _recordID_ and two simple blocks that are invoked on success or in case of an failure.

## Reset the entire store

The reset function allows you the erase all data available in the used Store object:

    // clears the entire store
    [myStore reset:^{
        // nope...
    } failure:^(NSError *error) {
        // when an error occurs... at least log it to the console..
        NSLog(@"Read: An error occured! \n%@", error);
    }];

The reset method accepts two simple blocks that are invoked on success or in case of an failure.

Authentication and User enrollment
==================================

## Creating an authenticator with an authentication module

To create an authenticator, you need to use the DefaultAuthenticator class. Below is an example: 

    // create an authenticator object
    Authenticator authenticator = new DefaultAuthenticator();

    // add a new auth module and the required 'base url':
    URL baseURL = null;
    try {    
        URL baseURL = new URL("https://todoauth-aerogear.rhcloud.com/todo-server/");
    } catch (MalformedURLException exception) {throw new RuntiemException(exception);}
   
    Authenticationmodule authMod = Authenticator.auth(AuthType.REST, baseURL).add();

The DefaultAuthenticator class offers some simple 'management' APIs to work with containing RestAuthenticationmodule objects. The API provides an authentication and enrollment API. The default implementation uses REST as the auth transport. Similar to the pipe API technical details of the underlying system are not exposed.

## Register a user

The _enroll_ function of the AuthenticationModule interface is used to register new users with the backend:

    // assemble the dictionary that has all the data for THIS particular user:
    MAP<String, String> userData = new HashMap<String, String>();
    userData.put("username","john");
    userData.put("password","123");
    [userData setValue:@"me@you.com" forKey:@"email"];
    [userData setValue:@"21sda812sad24" forKey:@"betaAccountToken"];
    

    // register a new user
    [myMod enroll:userData success:^(id data) {
        // after a successful _registration_, we can work
        // with the returned data...
        NSLog(@"We got: %@", data);
    } failure:^(NSError *error) {
        // when an error occurs... at least log it to the console..
        NSLog(@"SAVE: An error occured! \n%@", error);
    }];

The _enroll_ function submits a generic map object with contains all the information about the new user, that the server endpoint requires. The default (REST) auth module issues for the above a request against _https://todoauth-aerogear.rhcloud.com/todo-server/auth/register_. Besides the NSDictionary the function accepts two simple blocks that are invoked on success or in case of an failure.

## Login 

Once you have a _valid_ user you can use that information to issue a login against the server, to start accessing protected endpoints:

    // issuing a login
    [myMod login:@"john" password:@"123" success:^(id object) {
        // after a successful _login_, we can work
        // with the returned data...
    } failure:^(NSError *error) {
        // when an error occurs... at least log it to the console..
        NSLog(@"SAVE: An error occured! \n%@", error);
    }];

The default (REST) auth module issues for the above a request against _https://todoauth-aerogear.rhcloud.com/todo-server/auth/login_. Besides the _username_ and the _password_, the function accepts two simple blocks that are invoked on success or in case of an failure.

## Pass the auth module to a pipe

After running a successful login, you can start using the _RestAuthenticationmodule_ object on a _Pipe_ object to access protected endpoints:

    ...
    id<Pipe> tasks = [pipeline add:@"tasks" baseURL:serverURL authModule:myMod];

    [tasks read:^(id responseObject) {
        // LOG the JSON response, returned from the server:
        NSLog(@"READ RESPONSE\n%@", [responseObject description]);
    } failure:^(NSError *error) {
        // when an error occurs... at least log it to the console..
        NSLog(@"Read: An error occured! \n%@", error);
    }];

When creating a pipe you need to use the _authModule_ argument in order to pass in an _RestAuthenticationmodule_ object.

## Logout

The logout from the server can be archived by using the _logout_ function:

    // logout:
    [myMod logout:^{
        // after a successful _logout_, when can notify the application
    } failure:^(NSError *error) {
        // when an error occurs... at least log it to the console..
        NSLog(@"SAVE: An error occured! \n%@", error);
    }];

The default (REST) auth module issues for the above a request against _https://todoauth-aerogear.rhcloud.com/todo-server/auth/logout_. The function accepts two simple blocks that are invoked on success or in case of an failure.