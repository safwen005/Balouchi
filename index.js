  const functions = require('firebase-functions');
  const admin = require('firebase-admin');
  admin.initializeApp();
  const db = admin.firestore();
  const field=require('firebase-admin').firestore.FieldValue;
  const users=db.collection('users');
  const mydefault=users.doc('default');
  const chat=db.collection('chat');
  const notifications=db.collection('notifications');
  const products=db.collection('products');

 exports.getnews = functions.https.onCall((data, context) => {

      return products.doc('all').collection(data.categorie).orderBy('description.date','desc').get().then(col => {

             const result=[];

             col.docs.slice(data.start,data.start+data.step).forEach(doc => {
               result.push(little_product(doc.data()));
             });

             return result;
         })
    });
 exports.mnews = functions.https.onCall((data, context) => {



      const query=[];

     for(var i = 1; i < 7;i++){
           query.push(products.doc('all').collection(i.toString()).orderBy('description.date','desc').get());
      }

      return Promise.all(query).then(res => {

         const result=[];
           res.forEach(coll => {
             const items=[];
             coll.docs.slice(0,6).forEach(doc => {
                 items.push(little_product(doc.data()));
             });
             result.push(items);
           });
           return result;

      }).catch(error => {
        return error;
      })

     });
 exports.thelasts = functions.https.onCall((data, context) => {

        return users.doc(data.uid).get().then(res => {

             var conversations=[];
             const mycontacts=res.data().contacts;

             if (mycontacts.length==0)
               return false;
               else {
                 mycontacts.forEach(contact => {
                   conversations.push(chat.doc(contact.messages).collection('messages').orderBy('date','desc').get());
                 });
                 mycontacts.forEach(contact => {
                   conversations.push(users.doc(contact.with).get());
                 });

                 return Promise.all(conversations).then(ress => {
                  const myresult=[];
                  for(var i = 0; i < mycontacts.length;i++){
                       const result=(ress[i+mycontacts.length].data());
                       const online=((result.lastlogin==null) ? false : ((Date.now()-(result.lastlogin))<=60000));
                       myresult.push({
                          username:(result.personal_info).username,
                          picture:(result.personal_info.ppicture).path,
                          lastlogin:result.lastlogin,
                          lasts:((ress[i].docs)[0]).data(),
                          online:online,
                          path:(mycontacts[i]).messages,
                          token:result.token,
                          uid:result.uid
                       });

                   }
                   return myresult;
                 }).catch(error => {
                   return error;
                 })
               }

}).catch(error => {
  return error;
})

     });

 exports.sendit = functions.https.onCall((data, context) => {
      const mydata=data.conversation;
      const id=chat.doc().id;
      const query=[];
      mydata.date=Date.now();
      mydata.path="chat/"+data.path+"/messages/"+id;
      return chat.doc(data.path).collection('messages').doc(id).set(mydata).then(res => {
            if (mydata.token!=null)
       send_notification(mydata.token,(mydata.message!=null) ? 1 : 2,mydata.username,mydata.sender,data.path);
            return true;
          }).catch(error => {
            return error;
          })
    });

      function send_notification(token,type,username,uid,path){
        var body=null;

        switch (type) {
          case 0:
            body='قام بالتعليق على منتوجك !';
            break;
            case 1:
              body='أرسل لك رسالة !';
              break;
              case 2:
                body='أرسل لك صورة !';
        }


        admin.messaging().send({
      "token":token.toString(),
       "data":{
         "type":(type==0).toString(),
         "title":username.toString(),
         "body":body.toString(),
         "uid":uid.toString(),
         "path":path.toString()
       }
      });
      }

    exports.startchat = functions.https.onCall((data, context) => {

        return users.doc(data.uid1).get().then(res => {

          const contacts=res.data().contacts;

          var path=null;
          contacts.forEach(contact => {
              if (contact.with==data.uid2)
                 path=contact.messages;
          });
          if (path==null){
                 const id=chat.doc().id;
                 return Promise.all([chat.doc(id).collection('messages').doc(id).set({
                   date:Date.now(),
                   message:'يمكنكم التواصل الأن !',
                   sender:data.uid1,
                   img:null,
                   vue:false,
                   path:'chat/'+id+'/messages/'+id,
                   token:null,
                   username:null
                 }),
                 users.doc(data.uid1).update({contacts:field.arrayUnion({messages:id,with:data.uid2})}),
                 users.doc(data.uid2).update({contacts:field.arrayUnion({messages:id,with:data.uid1})})
               ]).then(ress => {
                    return id.toString();
               })
          }
          else return path.toString();

    })
  });
  exports.Credential = functions.https.onCall((data, context) => {

   return mydefault.get().then(res => {
    const myinfo=data.info;
    const query=[];
    myinfo.lastlogin=Date.now();

    query.push(users.doc(myinfo.uid).set(myinfo));
    query.push(products.doc(myinfo.uid).set(my_products(myinfo.uid)));
    query.push(notifications.doc('all').collection(myinfo.uid).doc().set(data.notifications));

    return Promise.all(query).then(res => {
      return true;
    }).catch(error => {
       return false;
     })

   })

  });
  exports.creatuser = functions.https.onCall((data, context) => {

     return mydefault.get().then(res => {

      const myinfo=data.info;
      const query=[];
      var number=null;
      var name=null;

      myinfo.lastlogin=Date.now();

      if (data.isphone){
        number=res.data().number+1;
        myinfo.personal_info.username='مجهول '+number;
        query.push(mydefault.update({number:field.increment(1)}));
      }
      name=myinfo.personal_info.username;

     query.push(users.doc(myinfo.uid).set(myinfo));
     query.push(admin.auth().updateUser(myinfo.uid,{displayName: name}));
     query.push(products.doc(myinfo.uid).set(my_products(myinfo.uid)));
     query.push(notifications.doc('all').collection(myinfo.uid).doc().set(data.notifications));

    return Promise.all(query).then(res => {
        return true;
    }).catch(function(error) {
      return false;
    })

   })

  });

 exports.update_profile = functions.https.onCall((data, context) => {

    return  admin.auth().updateUser(data.uid,data.info).then(user => {
          return true;
}).catch(error => {
  if (error.code=='auth/email-already-exists')
      return false;
})

});

  exports.getcomment = functions.https.onCall((data, context) => {

      return products.doc(data.path).get().then(doc => {


        const comments=(doc.data().comments.reverse()).slice(data.pos,data.pos+data.step);
        const myprofiles=[];

        if (comments.length>0){
        comments.forEach(cmnt => {
           myprofiles.push(users.doc(cmnt.profile).get());
        })

        return Promise.all(myprofiles).then(profiles => {

          for(var i = 0; i < profiles.length;i++){
               comments[i].profile=product_profile(profiles[i].data());
           }

           return comments;

        }).catch(error => {
         return error.toString();
        })

      }
      else return null;
      })


  });

  exports.theprod = functions.https.onCall((data, context) => {

    if (!data.exist)
    products.doc(data.path).update({vue:field.increment(1)});

    return products.doc(data.path).get().then(doc => {

            const mydata=doc.data();

            var myprofiles=[];

            myprofiles.push(mydata.profile.get());

            const test=(mydata.comments).length>0

            if (test){
            mydata.comments=mydata.comments.reverse().slice(0,2);
            mydata.comments.forEach(cmnt => {
               myprofiles.push(users.doc(cmnt.profile).get());
            });
           }

           return Promise.all(myprofiles).then(profiles => {

            mydata.profile=product_profile(profiles[0].data());
            if (test){
            for(var i = 0; i < profiles.length-1;i++){
                 mydata.comments[i].profile=product_profile(profiles[i+1].data());
             }
           }
            return mydata;

            }).catch(error => {
             return false;
            })
    })

  });

  exports.addcom = functions.https.onCall((data, context) => {

    const mydata=data.noti;
    const query=[];
    mydata.date=Date.now();
    query.push(products.doc(data.path).update({comments : field.arrayUnion(data.mycomment)}));
    query.push(notifications.doc('all').collection(data.receiver).doc().set(mydata));
         if (data.token!=null)
    send_notification(data.token,0,data.username,(data.mycomment).profile.toString(),data.path);
     return Promise.all(query).then(res => {
          return true;
     }).catch(error => {
       return error;
     })
  });

  exports.upcomment = functions.https.onCall((data, context) => {

      var comment=data.comment;
      return products.doc(data.path).get().then(res => {
      var mycomments=res.data().comments;
      mycomments[(mycomments.length-1)-(data.position)]=comment;
      return products.doc(data.path).update({comments:mycomments}).then(() => {

                              return users.doc(comment.profile).get(prf => {
                                 comment.profile=product_profile(prf.data());
                                 return comment;
                              }).catch(error => {
                                return false
                              })
            })
          }).catch(error => {
            return false;
          })

  });

  exports.myprod = functions.https.onCall((data, context) => {


   return products.doc(data.uid).get().then(res => {

     var list=null;
     const p=data.pos;

     switch(data.mine){
       case true :
       list=res.data().mine.slice(p,p+3);
       break;
       case false :
       list=res.data().favorite.slice(p,p+3);
       break;
       default:
       list=(res.data().mine.slice(p,p+2)).concat(res.data().favorite.slice(p,p+2));
     }


     if (list.length>0)
     {
       list.forEach(function(part, index) {
         this[index] = products.doc(part).get();
       }, list);

         return Promise.all(list).then(ress => {
               const mylist=ress;
               mylist.forEach(function(part, index) {
                 this[index] = little_product(part.data());
               }, mylist);

              if(data.mine == null )
                return {favorite:(mylist.slice((res.data().mine.slice(p,p+2)).length,mylist.length)) , mine:(mylist.slice(0,(res.data().mine.slice(p,p+2)).length)) };
              else return mylist;
         })
     }
     else return false;

   })
   });

  exports.searchprod = functions.https.onCall((data, context) => {

    const myinfo=data.info;
    const collections=[];

    if (myinfo.categorie==0){
       for(var i = 1; i <= 19;i++){
         collections.push(products.doc('all').collection(i.toString()).
         where('price', '>=', myinfo.min).
         where('price', '<=', myinfo.max).get());
       }
    }
    else {
      collections.push(products.doc('all').collection(myinfo.categorie.toString()).
      where('price', '>=', myinfo.min).
      where('price', '<=', myinfo.max).get());
    }

     return Promise.all(collections).then(docs=> {
       return filter(docs,myinfo);
     }).catch(error => {
       return error ;
     })

  });
  exports.search = functions.https.onCall((data, context) => {

      const collections=[];
      var result=[];

     for(var i = 1; i <= 19;i++){
        collections.push(products.doc('all').collection(i.toString()).get());
      }

       return Promise.all(collections).then(colls=> {
         if (data.location){
           colls.forEach(col => {
             col.docs.forEach(doc => {
               result.push(little_product(doc.data()));
             });
           });
         }
         else {
           colls.forEach(col => {
             col.docs.forEach(doc => {
               if ((doc.data().name).includes(data.name))
               result.push(little_product(doc.data()));
             });
           });
           result=(result.sort((a) => a.date).reverse());
         }

          return result;
       })

    });
  exports.deleteprod = functions.https.onCall((data, context) => {

  const query=[];
  var update=null;

  if (data.mine)
    update={mine : field.arrayRemove(data.path)};
  else
  update={favorite : field.arrayRemove(data.path)};

  return products.doc(data.uid).update(update).then(res => {
      if (!data.mine)
        return true;
      else {
        return products.get().then(cols => {

           const result=[];
           cols.docs.forEach(col => {
             if (Object.keys(col.data()).length>0)
               result.push(products.doc(col.data().path).update({favorite : field.arrayRemove(data.path)}));
           });
           result.push(products.doc(data.path).delete());

           return Promise.all(result).then(myres => {
               return true;
           })

        })
      }
  })

   });


  exports.add_product = functions.https.onCall((data, context) => {

    const id1=products.doc().id;

    const path='all/'+data.categorie+'/'+id1;

    const myproduct=data.product;

    myproduct.path=path;

    myproduct.profile=users.doc(data.uid);

    myproduct.description.date=Date.now();

    return Promise.all([
      products.doc(path).set(myproduct),
      products.doc(data.uid).update({mine : field.arrayUnion(path)})
    ]).then(res => {
        return path;
    }).catch(error => {
      return false;
    })

   });
  exports.update_product = functions.https.onCall((data, context) => {

  delete data.data.comments;
  delete data.data.profile;

  return  products.doc(data.path).update(data.data).then(res => {
      return true;
    }).catch(error => {
       return false;
     })
  });

  exports.byname = functions.https.onCall((data, context) => {
    const collections=[];
    for(var i = 1; i <= 19;i++){
         collections.push(products.doc('all').collection(i.toString()).get());
     }

    return filtername(collections,data.name);

  });

  function  filtername(collections,name){
    return Promise.all(collections).then(docs=> {
           const myresult=[];
           docs.forEach(col => {

             col.docs.forEach(doc => {

            if ((doc.data().name).includes(name))
            {
              myresult.push({
                name:(doc.data()).name,
                country:(doc.data().location).country,
                path:(doc.data()).path,
                picture:(doc.data().pictures.length)>0 ? doc.data().pictures[0].path : null
              });
            }

             });

            });
            return myresult;
    }).catch(error => {
      return error;
    })
  }
  function  little_product(doc){
         return  {
               name:doc.name,
               location:doc.location,
               price:doc.price.toString(),
               picture:(doc.pictures.length)>0 ? (doc.pictures[0].path) : null,
               path:doc.path,
               date:doc.description.date,
               vue:doc.vue
         }
   }
  function  product_profile(doc){
    return {
      username:doc.personal_info.username,
      picture:doc.personal_info.ppicture.path,
      lastlogin:doc.lastlogin,
      uid:doc.uid,
      phone:doc.personal_info.ptel,
      token:doc.token
    };
  }
  function  filter(docs,myinfo){

  var result=[];

  result=collectall(docs);

 if (myinfo.name!=null)
    result=result.filter(prod => prod.name.includes(myinfo.name));

 if (myinfo.country>0)
    result=result.filter(prod => (prod.location.country)==myinfo.country);

 if (myinfo.categorie>0)
    result=result.filter(prod => (prod.description.categorie)==myinfo.categorie);

 if (myinfo.condition>0)
     result=result.filter(prod => (prod.description.condition)==myinfo.condition);

 if (myinfo.garanty>0)
     result=result.filter(prod => (prod.description.garanty)==myinfo.garanty);

     result.forEach(function(part, index) {
       this[index] = little_product(result[index]);
     }, result);

    return result;

  }

  function  collectall(docs){
  const result=[];
  docs.forEach(col => {
    col.docs.forEach(doc => {
    result.push(doc.data());
  });
});
return result;
}

  function  my_products(path){
  return {
     favorite:[],
     mine:[],
     path:path
   };
}
