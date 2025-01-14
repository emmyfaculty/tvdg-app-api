<html>
<head>
    <style type="text/css">
        p {
        }

        p.title {
            font-weight: bold;
        }

        p.headline span, .text-bold {
            font-weight: bold;
        }

        /* -------------------------------------
               FOOTER
       ------------------------------------- */
        table.footer-wrap {
            width: 100%;
            clear: both !important;
        }

        .footer-wrap .container td.content p {
            border-top: 1px solid rgb(215, 215, 215);
            padding-top: 15px;
        }

        .footer-wrap .container td.content p {
            font-size: 10px;
            font-weight: bold;
        }
    </style>
</head>
<body>

<p class="title">Registration/Account Creation</p>
<p class="headline"><span>Headline:</span> New Account Confirmation </p>
<p>Dear ${EMAIL_FULLNAME}</p>
<p>Welcome to the DLE admin portal! An account has been created for you and you have been issued a temporary
    password.
    <br/>Your current login information is now:
</p>
<p>
    <br> <span class="text-bold">Username:</span><span>${EMAIL_USER_NAME}</span>
    <br><span class="text-bold">Password:</span><span>${EMAIL_USER_PASSWORD}</span>
</p>
<p>(You will have to change your password when you login for the first time)</p>
<p>To access your DLE Web App account, login at: </p>
<p><a href="${APP_URL}" target="_blank">${APP_URL}</a></p>

<p>Best Regards,</p>

<!-- FOOTER -->
<table class="footer-wrap">
    <tr>
        <td></td>
        <td class="container">
            <!-- content -->
            <div class="content">
                <p>
                    <table>
                        <tr>
                            <td align="center">
                <p>
                    ${EMAIL_DISCLAIMER}
                </p>
                <p>
                    ${EMAIL_SPAM_DISCLAIMER}
                </p>
                <p>
                    ${EMAIL_FOOTER_COPYRIGHT}
                </p>
        </td>
    </tr>
</table>
</p>
</div><!-- /content -->

</td>
<td></td>
</tr>
</table><!-- /FOOTER -->
</body>
</html>