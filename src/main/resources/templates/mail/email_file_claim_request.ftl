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
<p class="title">Request</p>
<p class="headline"><span>Headline:</span> File Claims </p>
<p>Dear Admin</p>
<p>There is a new File Claim request on the system. Please find the details of the request below:</p>
<p>
    <br> <span class="text-bold">Organisation:</span><span>${EMAIL_ORGANISATION_NAME}</span>
    <br><span class="text-bold">Tracking No:</span><span>${EMAIL_TRACKING_NUMBER}</span>
</p>
<p>Kindly attend to this as quickly as possible. </p>
<p>Many thanks,</p>
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
