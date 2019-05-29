# Text-Recognition
Android application to extract text from an image using firebase MLkit.

<img src="https://github.com/Samuel310/Text-Recognition/blob/master/img1.png" width="250px">  <img src="https://github.com/Samuel310/Text-Recognition/blob/master/img2.png" width="250px"> 

### Download sample app
[<img src="https://github.com/Samuel310/Text-Recognition/blob/master/AppLogo.jpg" title="Text_Recognition.apk" alt="Text_Recognition.apk" width="50px">](https://drive.google.com/file/d/17DwZRkeOALoQLPss6EBs9bt5bbrP9zPr/view?usp=sharing)
<br>Text_Recognition.apk

### Requirements
Android 5.1 (API level 22) or Above

## Implementation

Replace [google-services.json](https://github.com/Samuel310/Text-Recognition/blob/master/app/google-services.json) with your own google-services.json file which can be downloaded from your project created on firebase.
This json file is used to integrate your firebase project with this app.

```
{
  "project_info": {
    "project_number": "REPLACE WITH YOUR PROJECT NUMBER",
    "firebase_url": "REPLACE WITH YOUR URL",
    "project_id": "REPLACE WITH YOUR ID",
    "storage_bucket": "REPLACE WITH YOUR STORAGE"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "REPLACE WITH YOUR ID",
        "android_client_info": {
          "package_name": "com.samapps310.textrecognition"
        }
      },
      "oauth_client": [
        {
          "client_id": "REPLACE WITH YOUR ID",
          "client_type": 1,
          "android_info": {
            "package_name": "com.samapps310.textrecognition",
            "certificate_hash": "REPLACE WITH YOUR HASH"
          }
        },
        {
          "client_id": "REPLACE WITH YOUR ID",
          "client_type": 3
        }
      ],
      "api_key": [
        {
          "current_key": "REPLACE WITH YOUR KEY"
        }
      ],
      "services": {
        "appinvite_service": {
          "other_platform_oauth_client": [
            {
              "client_id": "REPLACE WITH YOUR ID",
              "client_type": 3
            }
          ]
        }
      }
    }
  ],
  "configuration_version": "1"
}
```

1. Replace this file
2. Build and Run application

you are good to go.

## License

This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/Samuel310/Text-Recognition/blob/master/LICENSE.md) file for details
