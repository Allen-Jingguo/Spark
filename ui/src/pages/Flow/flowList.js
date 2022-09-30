import { PageContainer } from '@ant-design/pro-components';
import {
  Space,
  Table,
  Tag,
  Button,
  Col,
  Form,
  Input,
  Row,
  Select,
  UpOutlined,
  DownOutlined,
  message,
  Popconfirm,
} from 'antd';
import {
  ProFormDatePicker,
  ProFormDateRangePicker,
  ProFormSelect,
  ProFormText,
  QueryFilter,
} from '@ant-design/pro-components';
import { Card } from 'antd';
import React, { useState, useEffect } from 'react';
import flowApi from '@/services/spark/flowApi';
import FlowModal from '@/pages/Flow/flowCreateModal';

const confirm = (record, type) => {
  console.log(record);
  if ('start' === type) {
    flowApi.start({ ...record }).then((result) => {
      console.log(result);
      if (result.success) {
        message.success('start success!');
      }
    });
  }

  if ('pause' === type) {
    flowApi.pause({ ...record }).then((result) => {
      console.log(result);
      if (result.success) {
        message.success('pause success!');
      }
    });
  }

  if ('stop' === type) {
    flowApi.stop({ ...record }).then((result) => {
      console.log(result);
      if (result.success) {
        message.success('Request received, flow is stopping, Please check the status later !');
      }
    });
  }
};

const cancel = (e) => {
  console.log(e);
  message.success('Canceled');
};

const columns = [
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
    render: (text) => <a>{text}</a>,
  },
  {
    title: 'InboundName',
    dataIndex: 'inboundName',
    key: 'inboundName',
  },
  {
    title: 'ParserName',
    dataIndex: 'parserName',
    key: 'parserName',
  },

  {
    title: 'FilterName',
    dataIndex: 'filterName',
    key: 'filterName',
    render: (text) => <a>{'NA'}</a>,
  },

  {
    title: 'KeyMapperName',
    dataIndex: 'keyMapperName',
    key: 'keyMapperName',
  },

  {
    title: 'FormatterName',
    dataIndex: 'formatterName',
    key: 'formatterName',
  },

  {
    title: 'OutboundName',
    dataIndex: 'outboundName',
    key: 'outboundName',
  },

  {
    title: 'Action',
    key: 'action',
    render: (_, record) => (
      <Space size="middle">
        <Popconfirm
          title="Are you sure to start this flow?"
          onConfirm={() => confirm(record, 'start')}
          onCancel={cancel}
          okText="Yes"
          cancelText="No"
        >
          <a href="#" style={{ fontSize: 15 }}>
            start
          </a>
        </Popconfirm>

        <Popconfirm
          title="Are you sure to start this flow?"
          onConfirm={() => confirm(record, 'pause')}
          onCancel={cancel}
          okText="Yes"
          cancelText="No"
        >
          <a href="#" style={{ fontSize: 15 }}>
            pause
          </a>
        </Popconfirm>

        <Popconfirm
          title="Are you sure to start this flow?"
          onConfirm={() => confirm(record, 'stop')}
          onCancel={cancel}
          okText="Yes"
          cancelText="No"
        >
          <a href="#" style={{ fontSize: 15 }}>
            stop
          </a>
        </Popconfirm>
      </Space>
    ),
  },
];

const FlowList = () => {
  const [form] = Form.useForm();
  const [flowList, setFlowList] = useState([]);
  const [showCreateModal, setShowCreateModal] = useState(false);

  useEffect(() => {}, []);

  const doSearch = (values) => {
    flowApi.getList(values).then((resp) => {
      setFlowList(resp.data);
    });
  };

  const closeCreateModal = () => {
    setShowCreateModal(false);
  };

  return (
    <PageContainer>
      <Card
        style={{
          borderRadius: 8,
        }}
        bodyStyle={{
          backgroundImage:
            'radial-gradient(circle at 97% 10%, #EBF2FF 0%, #F5F8FF 28%, #EBF1FF 124%)',
        }}
      >
        <div>
          <Form
            form={form}
            name="advanced_search"
            className="ant-advanced-search-form"
            onFinish={doSearch}
          >
            <Row gutter={24}>
              <Col>
                {/* span={24} */}
                {/* <Button type="primary" htmlType="submit">
                  Search
                </Button> */}

                <Form.Item
                  name="name"
                  label="Name"
                  // rules={[{ required: true, message: 'Missing name' }]}
                >
                  <Input />
                </Form.Item>
              </Col>
              <Col>
                {/* span={24} */}
                {/* <Button type="primary" htmlType="submit">
                  Search
                </Button> */}
                <Form.Item name="status" label="Status">
                  <Select
                    options={[
                      { label: 'New', value: 'New' },
                      { label: 'Running', value: 'Running' },
                      { label: 'Stopping', value: 'Stopping' },
                      { label: 'Paused', value: 'Paused' },
                      { label: 'Terimation', value: 'Terimation' },
                    ]}
                    style={{ width: 150 }}
                  />
                </Form.Item>
              </Col>
              <Col>
                <Form.Item name="type" label="Type">
                  <Select
                    options={[
                      { label: 'Default', value: 'default' },
                      { label: 'Transation', value: 'Transation' },
                    ]}
                    style={{ width: 150 }}
                  />
                </Form.Item>
              </Col>

              <Col
                //span={24}
                style={{
                  textAlign: 'left',
                }}
              >
                <Button type="primary" htmlType="submit">
                  Search
                </Button>
                <Button
                  style={{ marginLeft: 10 }}
                  type="primary"
                  onClick={() => {
                    form.resetFields();
                  }}
                >
                  Clear
                </Button>
                <Button
                  style={{ marginLeft: 10 }}
                  type="primary"
                  onClick={() => {
                    setShowCreateModal(true);
                  }}
                >
                  Create New
                </Button>
              </Col>
            </Row>
          </Form>
        </div>
        <div style={{ marginTop: 20 }}>
          <Table columns={columns} dataSource={flowList} pagination={true} />
        </div>
      </Card>
      {showCreateModal && <FlowModal closeCreateModal={closeCreateModal}></FlowModal>}
    </PageContainer>
  );
};
export default FlowList;
