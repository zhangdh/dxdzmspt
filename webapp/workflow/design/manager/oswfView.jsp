<link href="${webcontext}/workflow/design/js/css/style.css" type="text/css" rel="stylesheet">
<script charset="utf-8"  src="${webcontext}/workflow/design/js/common.js"></script>
<script  charset="utf-8"  src="${webcontext}/workflow/design/js/dtree.js"></script>
<script charset="utf-8"   src="${webcontext}/workflow/design/js/shape.js"></script>
<script charset="utf-8"  src="${webcontext}/workflow/design/js/toppanel.js"></script>
<script charset="utf-8" src="${webcontext}/workflow/design/js/topflow.js"></script>
<script charset="utf-8" src="${webcontext}/workflow/design/js/topflowevent.js"></script>
<script charset="utf-8"  src="${webcontext}/workflow/design/js/contextmenu.js"></script>

<div style='left:0px;top:0px;width:100%;height:100%;position:absolute;z-index:-1000' id="Canvas"></div>
<v:rect class="toolui" style="display:none" id="_rectui">
<v:Stroke dashstyle="dashdot"/>
</v:rect>
<v:rect class="toolui" style="display:none" fillcolor="#CCCCCC" id="_fillrectui">
<v:Stroke dashstyle="dashdot"/>
</v:rect>
<v:rect class="toolui" style="display:none" fillcolor="#CCCCCC" id="_splitrectui">
<v:Stroke dashstyle="dashdot"/>
</v:rect>
<v:roundrect class="toolui" style="display:none;left:0px;top:0px;width:60px;height:50px;" id="_roundrectui">
<v:Stroke dashstyle="dashdot"/>
</v:roundrect>
<v:shape type="#diamond" class="toolui" style="display:none;left:0px;top:0px;width:60px;height:50px;" strokeweight="1px" id="_diamondui">
<v:Stroke dashstyle="dashdot"/>
</v:shape>
<v:oval class="toolui" style="display:none;left:0px;top:0px;width:60px;height:50px;" id="_ovalui">
<v:Stroke dashstyle="dashdot"/>
</v:oval>
<v:line class="toolui" style="display:none" from="0,0" to="100,0" id="_lineui">
<v:Stroke dashstyle="dashdot" StartArrow="" EndArrow="Classic"/>
</v:line>