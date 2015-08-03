/******************************************************/
/* �ļ���:CCITCertCtrl.js                             */
/* ��  ��:                                    			  */
/* ��  ��: ���                                  	  */
/* ��  ��:2008-3-28                     						  */
/******************************************************/
document.write("<object classid=\"CLSID:936510B6-3A00-4616-9FD1-E77CAA733301\" id=\"CertControl\">");
document.write("</object>");

function dec2hex(decnum)
{
    var hexcode;
    var hexnum = "0123456789ABCDEF";
    var n;
    n = 0;
    hexcode = "";
    while (n < 16)
    {
    	i =decnum & 0x0000000F;
        hexcode = hexnum.substring(i, i + 1) + hexcode;
        decnum = decnum >> 4;
        n ++;
    }

    hexcode = "0x" + hexcode;
    return hexcode;
}

/*
���ܣ����ڼ���û��Ƿ�װ����ͨe�ܿͻ��˿ؼ���
˵������ҳ���Զ����ش˷���������û�������װ��ע������ͨe�ܿͻ��˿ؼ�������ʾ��������������
���룺��
�������
*/
function checkComSetuped()
{
   try
   {
      CertControl.test();
   }
   catch(ex)
   {
      alert("��û�а�װ��ͨe�ܿͻ��˿ؼ�!");
   }
}

/*
���ܣ��ж��Ƿ������ͨe�ܡ�
���룺��
����ֵ�����ϣ�1
				δ���ϣ�0
*/
function isUKConnect()
{
	var ret = CertControl.ISUKConnect();
	return ret;
}

/*
���ܣ����ڳ�ʼ����ͨe�ܡ�
���룺type ---- ��ʾ��ʵ������ʵ����֤����;��ĿǰĬ��Ϊ0
����ֵ���ɹ�������0
        ʧ�ܣ����ط�0
*/
function init(type)
{
	var ret=1;
	var iret=isUKConnect();
	if(iret!=1)
	{
		alert("û�в�����ͨe�ܣ�");
	}else
	{
		if (type == 0)
		{
			ret = CertControl.init("PROVIDE","","");
		}
		else
			{
				ret = CertControl.init("PROVIDE1","","");
			}
	}
	return ret;
}

/*
���ܣ�ȡ������֤�顣
���룺��
����ֵ��
				�ɹ�������֤��
				ʧ�ܣ��մ�
ע�⣺���ڳ�ʼ��������ʹ��
*/
function getSignCert()
{
	var signCert = 0;
	signCert = CertControl.getSignCert();
	return signCert;
}
/*
���ܣ�ȡ�ü���֤�顣ֻ������ʵ����֤��
���룺��
����ֵ��
				�ɹ�������֤��
				ʧ�ܣ��մ�
ע�⣺���ڳ�ʼ��������ʹ��
*/
function getEncCert()
{
	var encCert = 0;
	encCert = CertControl.getExchangeCert();
	return encCert;
}
/*
���ܣ�ȡ��֤��ӵ����������
���룺��
����ֵ��
				�ɹ���֤��ӵ��������
				ʧ�ܣ��մ�
ע�⣺���ڳ�ʼ��������ʹ��
*/
function getUserName()
{
	var userName = 0;
	userName = CertControl.GetUserName();
	return userName;
}

/*
���ܣ������Ľ�������ǩ����
���룺plain����ǩ��������
����ֵ���ɹ���ǩ�����ǩ��ֵ
				ʧ�ܣ��մ�
ע�⣺���ڳ�ʼ��������ʹ��
*/
function signNature(plain)
{
	var ret=0;
	var algorithm = "SHA1WITHRSA";//ǩ���㷨��ʾ
	ret = CertControl.signNature(plain,algorithm);
	return ret;
}
/*
���ܣ��ͷ���Դ��
���룺��
����ֵ����
*/
function end()
{
	CertControl.end();
}

/*
���ܣ�ȡ������֤����Ϣ������DN�����Ч�ڡ�
���룺signcert-----֤�飬base64����
����ֵ���ɹ�������֤����Ϣ
				ʧ�ܣ��մ�
*/
function getCertInfo(signcert)
{
	var ret=0;
	ret = CertControl.getCertInfo(signcert);
	return ret;
}
/*
���ܣ�ȡ������֤����չ����Ϣ��
���룺extendID------Ҫȡ�õ�֤��ID��ʶ
      certData-----֤�飬base64����
����ֵ���ɹ���֤����չ����Ϣ
				ʧ�ܣ��մ�
*/
function getCertExtend(extendID,certData)
{
	var ret = 0;
	ret = CertControl.GetCertExtends(extendID,certData);
	return ret;
}

/*
���ܣ�ȡ������֤������кš�
���룺signcert-----֤�飬base64����
����ֵ���ɹ�������֤�����к�
				ʧ�ܣ��մ�
*/
function getCertSN(signcert)
{
	var ret = 0;
	ret = CertControl.GetCertSN(signcert);
  return ret;
}

/*
���ܣ�ȡ������֤��İ䷢�ߡ�
���룺signcert-----֤�飬base64����
����ֵ���ɹ�������֤��䷢����Ϣ
				ʧ�ܣ��մ�
*/
function getCertIssuer(signcert)
{
	var ret = 0;
	ret = CertControl.GetCertIssuer(signcert);
	return ret;
}

/*
���ܣ���֤����ǩ����
���룺plain��ǩ�������ģ�signData��ǩ��ֵ��signCert��������֤ǩ��������֤��
����ֵ����֤ǩ�������
					�ɹ���0
					ʧ�ܣ���0
*/
function verifySign(plain,signData,signCert)
{
	var ret=1;
	ret = CertControl.verifySign(plain,signData,signCert,"SHA1WITHRSA");
	return ret;
}

/*
���ܣ�ȡ��ָ�����ȵ��������
���룺len��������ĳ��ȣ�������1-64֮��
����ֵ���ɹ��������
				ʧ�ܣ��մ�
*/
function genRandNum(len)
{
	var ret=0;
	ret = CertControl.genRandNum(len);
	return ret;
}
/*
���ܣ�ȡ���豸���кš�
���룺��
����ֵ���ɹ�:�豸���к�
				ʧ�ܣ��մ�
*/
function readUKSN()
{
	var ret=0;
	ret = CertControl.readUKSN();
  return ret;
}
/*
���ܣ�����û�֤����Ч�ԡ�
���룺certData-----��Ҫ����֤�飬base64����
����ֵ���ɹ�:����1
				ʧ�ܣ�����0
*/
function checkCert(certData)
{
	var ret = 0;
	ret = CertControl.checkCert(certData);
  return ret;
}
/*
���ܣ����������ŷ⡣
���룺types-----�����ŷ�Դ��Ϣ���ͣ����Ϊ0��ʾ�Ƕ�ĳһ�ļ��������ŷ⣬����Ϊ��ĳһ�ֽڴ��������ŷ⡣
      srcData-----�����ŷ�Դ����typesΪ0��srcData��ʾ�ļ��ľ���·������typesΪ������srcDataΪ��Ҫ�������ŷ��Դ��Ϣ��
      certData-----�����ߵ�����֤�飬��Ϊ˫֤����ҪΪ����֤�飬base64����
      outPath --- �����ŷ�ı����ַ��ȫ·��
����ֵ���ɹ�:��Ϊ�ļ������ء�SUCCESS�����ַ������������ŷ�ֵ
				ʧ�ܣ����ء�ERROR��
*/
function makeEnvlope(types, srcData, certData,outPath)
{
	var envData = 0;
	envData = CertControl.makeEnv(types, srcData, certData,outPath);
  return envData;
}

/*
���ܣ��������ŷ⡣�˽ӿ�����ַ����Ľ������ŷ�
���룺envData-----�����ŷ�ֵ��base64����
����ֵ���ɹ�:�����ŷ�ֵ
				ʧ�ܣ��մ�
*/
function openEnvlope(envData)
{
	var plain = 0;
	plain = CertControl.openEnv(envData);
  return plain;
}

/*
���ܣ��������ŷ⡣�˽ӿ�����ļ��Ľ������ŷ�
���룺inFilePath-----�����ŷ���ļ���ŵ�ַ
      outPath ----- ���ĵ����·��
����ֵ���ɹ�:0
				ʧ�ܣ�����
*/
function openEnvlopeFile(inFilePath,outPath)
{
	var plain = 0;
	plain = CertControl.openEnvFile(inFilePath,outPath);
  return plain;
}

/**********************************************************/
/*���������                                              */
/**********************************************************/
/*
*/
function writeFile(certData,fileData,fileType)
{
	var ret;
	try
	{
		ret = CertControl.writeFileFromHidden(certData,fileData,fileType);
   }
    catch(e)
    {
        if (e.description != "")
        {
           // alert("��������\r\n������:" + dec2hex(e.number) + "\r\n" + "����������" + e.description);
        }
        else
        {
           // alert("������:" + dec2hex(e.number));
        }
        ret = -1;
    }
   
  return ret;
}

function readFile(certData,fileType)
{
	var fileData = 0;
	try
	{
		fileData = CertControl.readFileFromHidden(certData,fileType);
   }
    catch(e)
    {
        if (e.description != "")
        {
           // alert("��������\r\n������:" + dec2hex(e.number) + "\r\n" + "����������" + e.description);
        }
        else
        {
            //alert("������:" + dec2hex(e.number));
        }
        fileData = "";
    }
  return fileData;
}

function deleteService(certData)
{
	var ret = 0;
	try
	{
		ret = CertControl.deleteService(certData);
   }
    catch(e)
    {
        if (e.description != "")
        {
           // alert("��������\r\n������:" + dec2hex(e.number) + "\r\n" + "����������" + e.description);
        }
        else
        {
           // alert("������:" + dec2hex(e.number));
        }
        ret = -1;
    }
  return ret;
}

function setRegisterInfo(registerInfo, registerType)
{
	var ret = 0;
	try
	{
		ret = CertControl.setRegisterInfo(registerInfo, registerType);
   }
    catch(e)
    {
        if (e.description != "")
        {
           alert("��������\r\n������:" + dec2hex(e.number) + "\r\n" + "����������" + e.description);
        }
        else
        {
           alert("������:" + dec2hex(e.number));
        }
        ret = -1;
    }
  return ret;
}